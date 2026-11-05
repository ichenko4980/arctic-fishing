package com.Zomfaka3.Tokpor.domain.model

enum class RewardTier(val requiredXp: Int) {
    ICE_SCOUT(100),
    FROST_ANGLER(300),
    DEEP_DRILLER(600),
    POLAR_MASTER(1000)
}

data class RewardTierState(
    val tier: RewardTier,
    val unlocked: Boolean,
    val claimed: Boolean
)
