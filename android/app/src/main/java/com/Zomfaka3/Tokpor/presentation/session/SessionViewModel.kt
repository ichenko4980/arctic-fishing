package com.Zomfaka3.Tokpor.presentation.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Zomfaka3.Tokpor.core.config.AppClock
import com.Zomfaka3.Tokpor.core.config.GameConfig
import com.Zomfaka3.Tokpor.domain.model.Catch
import com.Zomfaka3.Tokpor.domain.model.GearItem
import com.Zomfaka3.Tokpor.domain.model.SessionSummary
import com.Zomfaka3.Tokpor.domain.repository.SettingsRepository
import com.Zomfaka3.Tokpor.domain.usecase.EvaluateDailyChallengeUseCase
import com.Zomfaka3.Tokpor.domain.usecase.FinishSessionUseCase
import com.Zomfaka3.Tokpor.domain.usecase.ResolveCastUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

class SessionViewModel(
    private val settings: SettingsRepository,
    private val resolveCast: ResolveCastUseCase,
    private val finishSession: FinishSessionUseCase,
    private val dailyChallenge: EvaluateDailyChallengeUseCase
) : ViewModel() {

    private val state = MutableStateFlow(SessionUiState())

    val uiState: StateFlow<SessionUiState> = state.asStateFlow()

    private val finishEvents = Channel<SessionSummary>(Channel.BUFFERED)

    val events: Flow<SessionSummary> = finishEvents.receiveAsFlow()

    private var castJob: Job? = null
    private var revealJob: Job? = null
    private var closed = false

    init {
        viewModelScope.launch {
            settings.settings.collect { current ->
                state.update {
                    it.copy(
                        unitSystem = current.unitSystem,
                        reduceMotion = current.reduceMotion
                    )
                }
            }
        }
    }

    fun prepare() {
        castJob?.cancel()
        revealJob?.cancel()
        closed = false
        val current = settings.settings.value
        state.value = SessionUiState(
            unitSystem = current.unitSystem,
            reduceMotion = current.reduceMotion
        )
        refreshChance()
    }

    fun selectSpot(index: Int) {
        state.update { it.copy(spotIndex = index) }
        refreshChance()
    }

    fun selectWeather(index: Int) {
        state.update { it.copy(weatherIndex = index) }
        refreshChance()
    }

    fun toggleGear(item: GearItem) {
        state.update {
            val updated = if (item in it.checkedGear) {
                it.checkedGear - item
            } else {
                it.checkedGear + item
            }
            it.copy(checkedGear = updated)
        }
        refreshChance()
    }

    fun setPaused(paused: Boolean) {
        state.update { it.copy(paused = paused) }
    }

    fun dropLine() {
        val current = state.value
        if (closed || current.paused || current.phase == SessionPhase.CASTING) {
            return
        }
        if (current.castsLeft <= 0) {
            return
        }
        revealJob?.cancel()
        state.update {
            it.copy(
                phase = SessionPhase.CASTING,
                revealVisible = false,
                lastOutcome = null
            )
        }
        castJob = viewModelScope.launch {
            delay(GameConfig.CAST_RESOLVE_MS)
            val snapshot = state.value
            val outcome = resolveCast(
                spot = snapshot.spot,
                weather = snapshot.weather,
                gearChecked = snapshot.gearChecked,
                random = Random.Default
            )
            val logged = if (outcome.bite && outcome.species != null) {
                snapshot.catches + Catch(outcome.species, outcome.grams)
            } else {
                snapshot.catches
            }
            state.update {
                it.copy(
                    phase = SessionPhase.RESOLVED,
                    castsLeft = (it.castsLeft - 1).coerceAtLeast(0),
                    catches = logged,
                    lastOutcome = outcome,
                    revealVisible = outcome.bite
                )
            }
            if (outcome.bite) {
                revealJob = viewModelScope.launch {
                    delay(GameConfig.CATCH_REVEAL_MS)
                    state.update { it.copy(revealVisible = false) }
                }
            }
            if (state.value.castsLeft <= 0) {
                closeSession()
            }
        }
    }

    fun endSession() {
        castJob?.cancel()
        revealJob?.cancel()
        closeSession()
    }

    fun pause() {
        castJob?.cancel()
        revealJob?.cancel()
    }

    private fun closeSession() {
        if (closed) {
            return
        }
        closed = true
        val current = state.value
        val timestamp = AppClock.now()
        val day = AppClock.dayIndex(timestamp)
        dailyChallenge.record(
            day = day,
            catches = current.catches,
            gearChecked = current.gearChecked,
            weather = current.weather
        )
        val summary = finishSession(
            spot = current.spot,
            weather = current.weather,
            gearChecked = current.gearChecked,
            catches = current.catches,
            timestamp = timestamp,
            dayIndex = day
        )
        finishEvents.trySend(summary)
    }

    private fun refreshChance() {
        state.update {
            it.copy(
                biteChancePercent = (
                    resolveCast.biteChance(it.spot, it.weather, it.gearChecked) * 100f
                    ).roundToInt()
            )
        }
    }
}
