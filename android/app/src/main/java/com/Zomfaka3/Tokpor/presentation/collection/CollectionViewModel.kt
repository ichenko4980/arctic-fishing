package com.Zomfaka3.Tokpor.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Zomfaka3.Tokpor.data.sample.SampleData
import com.Zomfaka3.Tokpor.domain.model.SpeciesProgress
import com.Zomfaka3.Tokpor.domain.model.UnitSystem
import com.Zomfaka3.Tokpor.domain.repository.JournalRepository
import com.Zomfaka3.Tokpor.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CollectionUiState(
    val entries: List<SpeciesProgress> = emptyList(),
    val unitSystem: UnitSystem = UnitSystem.KILOGRAMS
) {
    val unlocked: Int
        get() = entries.count { it.unlocked }
}

class CollectionViewModel(
    private val journal: JournalRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    private val state = MutableStateFlow(CollectionUiState())

    val uiState: StateFlow<CollectionUiState> = state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val progress = journal.speciesProgress()
            state.update {
                it.copy(
                    entries = SampleData.species.map { species ->
                        progress[species.id] ?: SpeciesProgress(species.id, 0, 0)
                    },
                    unitSystem = settings.settings.value.unitSystem
                )
            }
        }
    }
}
