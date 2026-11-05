package com.Zomfaka3.Tokpor.domain.usecase

import com.Zomfaka3.Tokpor.domain.model.Catch
import com.Zomfaka3.Tokpor.domain.model.DailyChallengeKind
import com.Zomfaka3.Tokpor.domain.model.DailyState
import com.Zomfaka3.Tokpor.domain.model.SpeciesId
import com.Zomfaka3.Tokpor.domain.model.Weather
import com.Zomfaka3.Tokpor.domain.repository.JournalRepository

class EvaluateDailyChallengeUseCase(
    private val repository: JournalRepository,
    private val challenges: List<DailyChallengeKind>,
    private val bonusXp: Int
) {

    fun kindFor(day: Long): DailyChallengeKind =
        challenges[day.mod(challenges.size)]

    fun state(day: Long): DailyState = repository.dailyState(day, kindFor(day))

    fun record(day: Long, catches: List<Catch>, gearChecked: Int, weather: Weather) {
        val kind = kindFor(day)
        val achieved = when (kind) {
            DailyChallengeKind.LOG_THREE_CATCHES -> catches.size
            DailyChallengeKind.CHECK_ALL_GEAR -> gearChecked
            DailyChallengeKind.CATCH_TROPHY -> catches.count { it.species == SpeciesId.BURBOT }
            DailyChallengeKind.FINISH_IN_BLIZZARD -> if (weather == Weather.BLIZZARD) 1 else 0
        }
        val current = repository.dailyState(day, kind)
        if (achieved > current.progress) {
            repository.saveDailyProgress(day, achieved.coerceAtMost(kind.target))
        }
    }

    fun claim(day: Long): Int {
        val state = state(day)
        if (!state.complete || state.claimed) {
            return 0
        }
        repository.markDailyClaimed(day)
        val stats = repository.stats()
        repository.saveStats(stats.copy(totalXp = stats.totalXp + bonusXp))
        return bonusXp
    }
}
