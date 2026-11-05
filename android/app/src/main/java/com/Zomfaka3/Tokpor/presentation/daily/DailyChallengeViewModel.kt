package com.Zomfaka3.Tokpor.presentation.daily

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Zomfaka3.Tokpor.core.config.AppClock
import com.Zomfaka3.Tokpor.core.config.GameConfig
import com.Zomfaka3.Tokpor.domain.model.DailyState
import com.Zomfaka3.Tokpor.domain.usecase.EvaluateDailyChallengeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DailyUiState(
    val daily: DailyState? = null,
    val bonusXp: Int = GameConfig.DAILY_BONUS_XP
)

class DailyChallengeViewModel(
    private val dailyChallenge: EvaluateDailyChallengeUseCase
) : ViewModel() {

    private val state = MutableStateFlow(DailyUiState())

    val uiState: StateFlow<DailyUiState> = state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val today = dailyChallenge.state(AppClock.dayIndex())
            state.update { it.copy(daily = today) }
        }
    }

    fun claim() {
        viewModelScope.launch {
            dailyChallenge.claim(AppClock.dayIndex())
            val today = dailyChallenge.state(AppClock.dayIndex())
            state.update { it.copy(daily = today) }
        }
    }
}
