package com.Zomfaka3.Tokpor.core.di

import android.content.Context
import com.Zomfaka3.Tokpor.core.config.GameConfig
import com.Zomfaka3.Tokpor.data.local.JournalPreferences
import com.Zomfaka3.Tokpor.data.repository.JournalRepositoryImpl
import com.Zomfaka3.Tokpor.data.repository.SettingsRepositoryImpl
import com.Zomfaka3.Tokpor.data.sample.SampleData
import com.Zomfaka3.Tokpor.domain.repository.JournalRepository
import com.Zomfaka3.Tokpor.domain.repository.SettingsRepository
import com.Zomfaka3.Tokpor.domain.usecase.ClaimRewardUseCase
import com.Zomfaka3.Tokpor.domain.usecase.EvaluateDailyChallengeUseCase
import com.Zomfaka3.Tokpor.domain.usecase.FinishSessionUseCase
import com.Zomfaka3.Tokpor.domain.usecase.ResolveCastUseCase

object ServiceLocator {

    private var journal: JournalRepository? = null
    private var settings: SettingsRepository? = null
    private var finishSession: FinishSessionUseCase? = null
    private var claimReward: ClaimRewardUseCase? = null
    private var dailyChallenge: EvaluateDailyChallengeUseCase? = null

    val resolveCast: ResolveCastUseCase = ResolveCastUseCase(
        catalogue = SampleData.species,
        baseBite = GameConfig.BASE_BITE,
        gearBonus = GameConfig.GEAR_BITE_BONUS
    )

    fun init(context: Context) {
        if (journal != null) {
            return
        }
        val preferences = JournalPreferences(context.applicationContext)
        val journalRepository = JournalRepositoryImpl(preferences)
        journal = journalRepository
        settings = SettingsRepositoryImpl(preferences)
        finishSession = FinishSessionUseCase(
            repository = journalRepository,
            speciesXp = SampleData.speciesXp,
            sessionXp = GameConfig.XP_PER_SESSION
        )
        claimReward = ClaimRewardUseCase(journalRepository)
        dailyChallenge = EvaluateDailyChallengeUseCase(
            repository = journalRepository,
            challenges = SampleData.challenges,
            bonusXp = GameConfig.DAILY_BONUS_XP
        )
    }

    val journalRepository: JournalRepository
        get() = requireNotNull(journal)

    val settingsRepository: SettingsRepository
        get() = requireNotNull(settings)

    val finishSessionUseCase: FinishSessionUseCase
        get() = requireNotNull(finishSession)

    val claimRewardUseCase: ClaimRewardUseCase
        get() = requireNotNull(claimReward)

    val dailyChallengeUseCase: EvaluateDailyChallengeUseCase
        get() = requireNotNull(dailyChallenge)

    val viewModelFactory: AppViewModelFactory
        get() = AppViewModelFactory()
}
