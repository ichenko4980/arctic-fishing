package com.Zomfaka3.Tokpor.core.navigation

import com.Zomfaka3.Tokpor.domain.model.SessionSummary

sealed interface Screen {

    data object Splash : Screen

    data object Menu : Screen

    data object Session : Screen

    data class SessionResult(val summary: SessionSummary) : Screen

    data object DailyChallenge : Screen

    data object Progress : Screen

    data object Rewards : Screen

    data object Collection : Screen

    data object Settings : Screen
}
