package com.Zomfaka3.Tokpor.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Zomfaka3.Tokpor.core.config.GameConfig
import com.Zomfaka3.Tokpor.domain.model.UnitSystem
import com.Zomfaka3.Tokpor.domain.repository.JournalRepository
import com.Zomfaka3.Tokpor.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SessionResultUiState(
    val level: Int = 1,
    val totalXp: Int = 0,
    val xpInLevel: Int = 0,
    val xpPerLevel: Int = GameConfig.XP_PER_LEVEL,
    val unitSystem: UnitSystem = UnitSystem.KILOGRAMS
) {
    val levelFraction: Float
        get() = xpInLevel.toFloat() / xpPerLevel.toFloat()
}

class SessionResultViewModel(
    private val journal: JournalRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    private val state = MutableStateFlow(SessionResultUiState())

    val uiState: StateFlow<SessionResultUiState> = state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val stats = journal.stats()
            val level = (1 + stats.totalXp / GameConfig.XP_PER_LEVEL)
                .coerceAtMost(GameConfig.MAX_LEVEL)
            state.update {
                it.copy(
                    level = level,
                    totalXp = stats.totalXp,
                    xpInLevel = stats.totalXp % GameConfig.XP_PER_LEVEL,
                    unitSystem = settings.settings.value.unitSystem
                )
            }
        }
    }
}
