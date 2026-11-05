package com.Zomfaka3.Tokpor.domain.repository

import com.Zomfaka3.Tokpor.domain.model.AppSettings
import com.Zomfaka3.Tokpor.domain.model.UnitSystem
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {

    val settings: StateFlow<AppSettings>

    fun setUnitSystem(unitSystem: UnitSystem)

    fun setReduceMotion(enabled: Boolean)
}
