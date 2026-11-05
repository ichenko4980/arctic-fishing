package com.Zomfaka3.Tokpor.domain.model

data class SessionRecord(
    val timestamp: Long,
    val spot: Spot,
    val weather: Weather,
    val catches: Int,
    val grams: Int,
    val xp: Int
)

data class SessionSummary(
    val catches: Int,
    val grams: Int,
    val xp: Int,
    val bestSpecies: SpeciesId?,
    val bestGrams: Int,
    val unlockedTier: RewardTier?
)
