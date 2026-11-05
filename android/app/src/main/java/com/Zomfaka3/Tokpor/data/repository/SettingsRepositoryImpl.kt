package com.Zomfaka3.Tokpor.data.repository

import com.Zomfaka3.Tokpor.data.local.JournalPreferences
import com.Zomfaka3.Tokpor.domain.model.AppSettings
import com.Zomfaka3.Tokpor.domain.model.UnitSystem
import com.Zomfaka3.Tokpor.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsRepositoryImpl(
    private val preferences: JournalPreferences
) : SettingsRepository {

    private val state = MutableStateFlow(
        AppSettings(
            unitSystem = if (preferences.readBoolean(KEY_POUNDS)) {
                UnitSystem.POUNDS
            } else {
                UnitSystem.KILOGRAMS
            },
            reduceMotion = preferences.readBoolean(KEY_REDUCE_MOTION)
        )
    )

    override val settings: StateFlow<AppSettings> = state.asStateFlow()

    override fun setUnitSystem(unitSystem: UnitSystem) {
        preferences.writeBoolean(KEY_POUNDS, unitSystem == UnitSystem.POUNDS)
        state.update { it.copy(unitSystem = unitSystem) }
    }

    override fun setReduceMotion(enabled: Boolean) {
        preferences.writeBoolean(KEY_REDUCE_MOTION, enabled)
        state.update { it.copy(reduceMotion = enabled) }
    }

    private companion object {
        const val KEY_POUNDS = "unit_pounds"
        const val KEY_REDUCE_MOTION = "reduce_motion"
    }
}
