package com.Zomfaka3.Tokpor.domain.model

data class AnglerStats(
    val totalXp: Int = 0,
    val sessions: Int = 0,
    val catches: Int = 0,
    val heaviestGrams: Int = 0,
    val streak: Int = 0,
    val lastSessionDay: Long = 0L,
    val gearScore: Int = 0,
    val bestSpot: Spot? = null,
    val claimedTiers: Set<RewardTier> = emptySet()
)
