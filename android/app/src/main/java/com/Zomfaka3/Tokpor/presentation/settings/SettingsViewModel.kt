package com.Zomfaka3.Tokpor.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Zomfaka3.Tokpor.domain.model.AppSettings
import com.Zomfaka3.Tokpor.domain.model.UnitSystem
import com.Zomfaka3.Tokpor.domain.repository.JournalRepository
import com.Zomfaka3.Tokpor.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val settings: AppSettings = AppSettings(),
    val confirmReset: Boolean = false,
    val totalXp: Int = 0
)

class SettingsViewModel(
    private val journal: JournalRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    private val state = MutableStateFlow(SettingsUiState())

    val uiState: StateFlow<SettingsUiState> = state.asStateFlow()

    init {
        viewModelScope.launch {
            settings.settings.collect { current ->
                state.update { it.copy(settings = current) }
            }
        }
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val stats = journal.stats()
            state.update { it.copy(totalXp = stats.totalXp) }
        }
    }

    fun setPounds(enabled: Boolean) {
        settings.setUnitSystem(if (enabled) UnitSystem.POUNDS else UnitSystem.KILOGRAMS)
    }

    fun setReduceMotion(enabled: Boolean) {
        settings.setReduceMotion(enabled)
    }

    fun askReset() {
        state.update { it.copy(confirmReset = true) }
    }

    fun cancelReset() {
        state.update { it.copy(confirmReset = false) }
    }

    fun confirmReset() {
        viewModelScope.launch {
            journal.reset()
            state.update { it.copy(confirmReset = false, totalXp = 0) }
        }
    }
}
