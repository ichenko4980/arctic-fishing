package com.Zomfaka3.Tokpor.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Zomfaka3.Tokpor.presentation.app.AppViewModel
import com.Zomfaka3.Tokpor.presentation.collection.CollectionViewModel
import com.Zomfaka3.Tokpor.presentation.daily.DailyChallengeViewModel
import com.Zomfaka3.Tokpor.presentation.gameover.SessionResultViewModel
import com.Zomfaka3.Tokpor.presentation.menu.MenuViewModel
import com.Zomfaka3.Tokpor.presentation.progress.ProgressViewModel
import com.Zomfaka3.Tokpor.presentation.rewards.RewardsViewModel
import com.Zomfaka3.Tokpor.presentation.session.SessionViewModel
import com.Zomfaka3.Tokpor.presentation.settings.SettingsViewModel
import com.Zomfaka3.Tokpor.presentation.splash.SplashViewModel

class AppViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) ->
                AppViewModel()

            modelClass.isAssignableFrom(SplashViewModel::class.java) ->
                SplashViewModel()

            modelClass.isAssignableFrom(MenuViewModel::class.java) ->
                MenuViewModel(
                    journal = ServiceLocator.journalRepository,
                    settings = ServiceLocator.settingsRepository,
                    dailyChallenge = ServiceLocator.dailyChallengeUseCase
                )

            modelClass.isAssignableFrom(SessionViewModel::class.java) ->
                SessionViewModel(
                    settings = ServiceLocator.settingsRepository,
                    resolveCast = ServiceLocator.resolveCast,
                    finishSession = ServiceLocator.finishSessionUseCase,
                    dailyChallenge = ServiceLocator.dailyChallengeUseCase
                )

            modelClass.isAssignableFrom(SessionResultViewModel::class.java) ->
                SessionResultViewModel(
                    journal = ServiceLocator.journalRepository,
                    settings = ServiceLocator.settingsRepository
                )

            modelClass.isAssignableFrom(DailyChallengeViewModel::class.java) ->
                DailyChallengeViewModel(ServiceLocator.dailyChallengeUseCase)

            modelClass.isAssignableFrom(ProgressViewModel::class.java) ->
                ProgressViewModel(
                    journal = ServiceLocator.journalRepository,
                    settings = ServiceLocator.settingsRepository
                )

            modelClass.isAssignableFrom(RewardsViewModel::class.java) ->
                RewardsViewModel(
                    journal = ServiceLocator.journalRepository,
                    claimReward = ServiceLocator.claimRewardUseCase
                )

            modelClass.isAssignableFrom(CollectionViewModel::class.java) ->
                CollectionViewModel(
                    journal = ServiceLocator.journalRepository,
                    settings = ServiceLocator.settingsRepository
                )

            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(
                    journal = ServiceLocator.journalRepository,
                    settings = ServiceLocator.settingsRepository
                )

            else -> throw IllegalArgumentException(modelClass.name)
        }
        @Suppress("UNCHECKED_CAST")
        return created as T
    }
}
