package com.Zomfaka3.Tokpor.presentation.session

import com.Zomfaka3.Tokpor.core.config.GameConfig
import com.Zomfaka3.Tokpor.data.sample.SampleData
import com.Zomfaka3.Tokpor.domain.model.CastOutcome
import com.Zomfaka3.Tokpor.domain.model.Catch
import com.Zomfaka3.Tokpor.domain.model.GearItem
import com.Zomfaka3.Tokpor.domain.model.Spot
import com.Zomfaka3.Tokpor.domain.model.UnitSystem
import com.Zomfaka3.Tokpor.domain.model.Weather

enum class SessionPhase {
    SETUP,
    CASTING,
    RESOLVED
}

data class SessionUiState(
    val phase: SessionPhase = SessionPhase.SETUP,
    val spots: List<Spot> = SampleData.spots,
    val weatherOptions: List<Weather> = SampleData.weather,
    val gearOptions: List<GearItem> = SampleData.gear,
    val spotIndex: Int = 0,
    val weatherIndex: Int = 0,
    val checkedGear: Set<GearItem> = emptySet(),
    val castsLeft: Int = GameConfig.CASTS_PER_SESSION,
    val catches: List<Catch> = emptyList(),
    val lastOutcome: CastOutcome? = null,
    val revealVisible: Boolean = false,
    val paused: Boolean = false,
    val biteChancePercent: Int = 0,
    val unitSystem: UnitSystem = UnitSystem.KILOGRAMS,
    val reduceMotion: Boolean = false
) {
    val gearChecked: Int
        get() = checkedGear.size

    val castsUsed: Int
        get() = GameConfig.CASTS_PER_SESSION - castsLeft

    val totalGrams: Int
        get() = catches.sumOf { it.grams }

    val spot: Spot
        get() = spots[spotIndex]

    val weather: Weather
        get() = weatherOptions[weatherIndex]
}
