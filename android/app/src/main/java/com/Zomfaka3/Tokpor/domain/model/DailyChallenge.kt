package com.Zomfaka3.Tokpor.domain.model

enum class DailyChallengeKind(val target: Int) {
    LOG_THREE_CATCHES(3),
    CHECK_ALL_GEAR(5),
    CATCH_TROPHY(1),
    FINISH_IN_BLIZZARD(1)
}

data class DailyState(
    val kind: DailyChallengeKind,
    val progress: Int,
    val claimed: Boolean
) {
    val complete: Boolean
        get() = progress >= kind.target

    val fraction: Float
        get() = (progress.toFloat() / kind.target.toFloat()).coerceIn(0f, 1f)
}
