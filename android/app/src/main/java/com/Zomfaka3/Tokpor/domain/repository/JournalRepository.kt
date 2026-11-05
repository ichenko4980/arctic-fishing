package com.Zomfaka3.Tokpor.domain.repository

import com.Zomfaka3.Tokpor.domain.model.AnglerStats
import com.Zomfaka3.Tokpor.domain.model.DailyChallengeKind
import com.Zomfaka3.Tokpor.domain.model.DailyState
import com.Zomfaka3.Tokpor.domain.model.SessionRecord
import com.Zomfaka3.Tokpor.domain.model.SpeciesId
import com.Zomfaka3.Tokpor.domain.model.SpeciesProgress

interface JournalRepository {

    fun stats(): AnglerStats

    fun saveStats(stats: AnglerStats)

    fun speciesProgress(): Map<SpeciesId, SpeciesProgress>

    fun saveSpeciesProgress(progress: Map<SpeciesId, SpeciesProgress>)

    fun recentSessions(): List<SessionRecord>

    fun appendSession(record: SessionRecord)

    fun dailyState(day: Long, kind: DailyChallengeKind): DailyState

    fun saveDailyProgress(day: Long, progress: Int)

    fun markDailyClaimed(day: Long)

    fun reset()
}
