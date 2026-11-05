package com.Zomfaka3.Tokpor.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Zomfaka3.Tokpor.core.config.AppClock
import com.Zomfaka3.Tokpor.core.config.GameConfig
import com.Zomfaka3.Tokpor.domain.model.RewardTier
import com.Zomfaka3.Tokpor.domain.repository.JournalRepository
import com.Zomfaka3.Tokpor.domain.repository.SettingsRepository
import com.Zomfaka3.Tokpor.domain.usecase.EvaluateDailyChallengeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(
    private val journal: JournalRepository,
    private val settings: SettingsRepository,
    private val dailyChallenge: EvaluateDailyChallengeUseCase
) : ViewModel() {

    private val state = MutableStateFlow(MenuUiState())

    val uiState: StateFlow<MenuUiState> = state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val stats = journal.stats()
            val species = journal.speciesProgress()
            val daily = dailyChallenge.state(AppClock.dayIndex())
            val ready = RewardTier.entries.count {
                stats.totalXp >= it.requiredXp && it !in stats.claimedTiers
            }
            val next = RewardTier.entries.firstOrNull { stats.totalXp < it.requiredXp }
            val level = (1 + stats.totalXp / GameConfig.XP_PER_LEVEL)
                .coerceAtMost(GameConfig.MAX_LEVEL)
            state.update {
                it.copy(
                    level = level,
                    totalXp = stats.totalXp,
                    sessions = stats.sessions,
                    speciesUnlocked = species.values.count { entry -> entry.unlocked },
                    speciesTotal = species.size,
                    daily = daily,
                    rewardsReady = ready,
                    nextTier = next,
                    reduceMotion = settings.settings.value.reduceMotion
                )
            }
        }
    }
}
