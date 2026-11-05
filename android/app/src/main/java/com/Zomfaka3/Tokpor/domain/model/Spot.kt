package com.Zomfaka3.Tokpor.domain.model

enum class Spot(
    val biteBonus: Float,
    val massMultiplier: Float,
    val trophyShift: Int
) {
    FROZEN_BAY(0.05f, 1.00f, 0),
    DEEP_CRACK(0.00f, 1.00f, 10),
    NORTH_SHELF(-0.04f, 1.15f, 0)
}
