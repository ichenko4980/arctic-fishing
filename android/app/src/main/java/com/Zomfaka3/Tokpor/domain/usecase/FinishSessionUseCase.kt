package com.Zomfaka3.Tokpor.domain.usecase

import com.Zomfaka3.Tokpor.domain.model.AnglerStats
import com.Zomfaka3.Tokpor.domain.model.Catch
import com.Zomfaka3.Tokpor.domain.model.RewardTier
import com.Zomfaka3.Tokpor.domain.model.SessionRecord
import com.Zomfaka3.Tokpor.domain.model.SessionSummary
import com.Zomfaka3.Tokpor.domain.model.SpeciesId
import com.Zomfaka3.Tokpor.domain.model.SpeciesProgress
import com.Zomfaka3.Tokpor.domain.model.Spot
import com.Zomfaka3.Tokpor.domain.model.Weather
import com.Zomfaka3.Tokpor.domain.repository.JournalRepository

class FinishSessionUseCase(
    private val repository: JournalRepository,
    private val speciesXp: Map<SpeciesId, Int>,
    private val sessionXp: Int
) {

    operator fun invoke(
        spot: Spot,
        weather: Weather,
        gearChecked: Int,
        catches: List<Catch>,
        timestamp: Long,
        dayIndex: Long
    ): SessionSummary {
        val grams = catches.sumOf { it.grams }
        val catchXp = catches.sumOf { speciesXp[it.species] ?: 0 }
        val earnedXp = catchXp + sessionXp
        val best = catches.maxByOrNull { it.grams }

        val previous = repository.stats()
        val previousTiers = RewardTier.entries.filter { previous.totalXp >= it.requiredXp }.toSet()
        val streak = when (previous.lastSessionDay) {
            dayIndex -> previous.streak.coerceAtLeast(1)
            dayIndex - 1L -> previous.streak + 1
            else -> 1
        }
        val updated = previous.copy(
            totalXp = previous.totalXp + earnedXp,
            sessions = previous.sessions + 1,
            catches = previous.catches + catches.size,
            heaviestGrams = maxOf(previous.heaviestGrams, best?.grams ?: 0),
            streak = streak,
            lastSessionDay = dayIndex,
            gearScore = maxOf(previous.gearScore, gearChecked),
            bestSpot = if (catches.size >= averageCatches(previous)) spot else previous.bestSpot
        )
        repository.saveStats(updated)

        val progress = repository.speciesProgress().toMutableMap()
        catches.forEach { logged ->
            val current = progress[logged.species]
            progress[logged.species] = SpeciesProgress(
                id = logged.species,
                count = (current?.count ?: 0) + 1,
                bestGrams = maxOf(current?.bestGrams ?: 0, logged.grams)
            )
        }
        repository.saveSpeciesProgress(progress)

        repository.appendSession(
            SessionRecord(
                timestamp = timestamp,
                spot = spot,
                weather = weather,
                catches = catches.size,
                grams = grams,
                xp = earnedXp
            )
        )

        val newTier = RewardTier.entries
            .filter { updated.totalXp >= it.requiredXp && it !in previousTiers }
            .maxByOrNull { it.requiredXp }

        return SessionSummary(
            catches = catches.size,
            grams = grams,
            xp = earnedXp,
            bestSpecies = best?.species,
            bestGrams = best?.grams ?: 0,
            unlockedTier = newTier
        )
    }

    private fun averageCatches(stats: AnglerStats): Int =
        if (stats.sessions == 0) 0 else stats.catches / stats.sessions
}
