package com.Zomfaka3.Tokpor.presentation.menu

import com.Zomfaka3.Tokpor.domain.model.DailyState
import com.Zomfaka3.Tokpor.domain.model.RewardTier

data class MenuUiState(
    val level: Int = 1,
    val totalXp: Int = 0,
    val sessions: Int = 0,
    val speciesUnlocked: Int = 0,
    val speciesTotal: Int = 0,
    val daily: DailyState? = null,
    val rewardsReady: Int = 0,
    val nextTier: RewardTier? = null,
    val reduceMotion: Boolean = false
)
