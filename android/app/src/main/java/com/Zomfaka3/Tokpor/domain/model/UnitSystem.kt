package com.Zomfaka3.Tokpor.domain.model

enum class UnitSystem {
    KILOGRAMS,
    POUNDS
}

data class AppSettings(
    val unitSystem: UnitSystem = UnitSystem.KILOGRAMS,
    val reduceMotion: Boolean = false
)
