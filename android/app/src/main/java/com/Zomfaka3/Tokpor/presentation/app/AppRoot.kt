package com.Zomfaka3.Tokpor.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Zomfaka3.Tokpor.core.di.ServiceLocator
import com.Zomfaka3.Tokpor.core.navigation.Screen
import com.Zomfaka3.Tokpor.core.theme.FrostWhite
import com.Zomfaka3.Tokpor.presentation.collection.CollectionScreen
import com.Zomfaka3.Tokpor.presentation.daily.DailyChallengeScreen
import com.Zomfaka3.Tokpor.presentation.gameover.SessionResultScreen
import com.Zomfaka3.Tokpor.presentation.menu.MenuScreen
import com.Zomfaka3.Tokpor.presentation.progress.ProgressScreen
import com.Zomfaka3.Tokpor.presentation.rewards.RewardsScreen
import com.Zomfaka3.Tokpor.presentation.session.SessionScreen
import com.Zomfaka3.Tokpor.presentation.settings.SettingsScreen
import com.Zomfaka3.Tokpor.presentation.splash.SplashScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()

    BackHandler(enabled = screen !is Screen.Menu && screen !is Screen.Splash) {
        appViewModel.back()
    }

    Surface(modifier = Modifier.fillMaxSize(), color = FrostWhite) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                fadeIn(animationSpec = tween(durationMillis = 300)) togetherWith
                    fadeOut(animationSpec = tween(durationMillis = 200))
            }
        ) { target ->
            when (target) {
                is Screen.Splash -> SplashScreen(
                    onFinished = { appViewModel.finishSplash() }
                )

                is Screen.Menu -> MenuScreen(
                    onStartSession = { appViewModel.navigate(Screen.Session) },
                    onDaily = { appViewModel.navigate(Screen.DailyChallenge) },
                    onProgress = { appViewModel.navigate(Screen.Progress) },
                    onCollection = { appViewModel.navigate(Screen.Collection) },
                    onRewards = { appViewModel.navigate(Screen.Rewards) },
                    onSettings = { appViewModel.navigate(Screen.Settings) }
                )

                is Screen.Session -> SessionScreen(
                    onMenu = { appViewModel.home() },
                    onFinished = { summary ->
                        appViewModel.replaceCurrent(Screen.SessionResult(summary))
                    }
                )

                is Screen.SessionResult -> SessionResultScreen(
                    summary = target.summary,
                    onLogAgain = { appViewModel.replaceCurrent(Screen.Session) },
                    onMenu = { appViewModel.home() }
                )

                is Screen.DailyChallenge -> DailyChallengeScreen(
                    onBack = { appViewModel.back() }
                )

                is Screen.Progress -> ProgressScreen(
                    onBack = { appViewModel.back() }
                )

                is Screen.Rewards -> RewardsScreen(
                    onBack = { appViewModel.back() }
                )

                is Screen.Collection -> CollectionScreen(
                    onBack = { appViewModel.back() }
                )

                is Screen.Settings -> SettingsScreen(
                    onBack = { appViewModel.back() }
                )
            }
        }
    }
}
