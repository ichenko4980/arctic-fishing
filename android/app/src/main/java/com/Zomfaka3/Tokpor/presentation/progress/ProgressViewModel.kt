package com.Zomfaka3.Tokpor.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Zomfaka3.Tokpor.core.config.GameConfig
import com.Zomfaka3.Tokpor.domain.model.AnglerStats
import com.Zomfaka3.Tokpor.domain.model.RewardTier
import com.Zomfaka3.Tokpor.domain.model.SessionRecord
import com.Zomfaka3.Tokpor.domain.model.UnitSystem
import com.Zomfaka3.Tokpor.domain.repository.JournalRepository
import com.Zomfaka3.Tokpor.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProgressUiState(
    val stats: AnglerStats = AnglerStats(),
    val level: Int = 1,
    val xpInLevel: Int = 0,
    val xpPerLevel: Int = GameConfig.XP_PER_LEVEL,
    val title: RewardTier? = null,
    val sessions: List<SessionRecord> = emptyList(),
    val unitSystem: UnitSystem = UnitSystem.KILOGRAMS
) {
    val levelFraction: Float
        get() = xpInLevel.toFloat() / xpPerLevel.toFloat()
}

class ProgressViewModel(
    private val journal: JournalRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    private val state = MutableStateFlow(ProgressUiState())

    val uiState: StateFlow<ProgressUiState> = state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val stats = journal.stats()
            val level = (1 + stats.totalXp / GameConfig.XP_PER_LEVEL)
                .coerceAtMost(GameConfig.MAX_LEVEL)
            val title = RewardTier.entries
                .filter { it in stats.claimedTiers }
                .maxByOrNull { it.requiredXp }
            state.update {
                it.copy(
                    stats = stats,
                    level = level,
                    xpInLevel = stats.totalXp % GameConfig.XP_PER_LEVEL,
                    title = title,
                    sessions = journal.recentSessions(),
                    unitSystem = settings.settings.value.unitSystem
                )
            }
        }
    }
}
