package com.Zomfaka3.Tokpor.domain.model

enum class Weather(val biteBonus: Float) {
    CLEAR(0.04f),
    SNOW(0.00f),
    BLIZZARD(-0.08f)
}
