package com.Zomfaka3.Tokpor.data.repository

import com.Zomfaka3.Tokpor.core.config.GameConfig
import com.Zomfaka3.Tokpor.data.local.JournalPreferences
import com.Zomfaka3.Tokpor.domain.model.AnglerStats
import com.Zomfaka3.Tokpor.domain.model.DailyChallengeKind
import com.Zomfaka3.Tokpor.domain.model.DailyState
import com.Zomfaka3.Tokpor.domain.model.RewardTier
import com.Zomfaka3.Tokpor.domain.model.SessionRecord
import com.Zomfaka3.Tokpor.domain.model.SpeciesId
import com.Zomfaka3.Tokpor.domain.model.SpeciesProgress
import com.Zomfaka3.Tokpor.domain.model.Spot
import com.Zomfaka3.Tokpor.domain.model.Weather
import com.Zomfaka3.Tokpor.domain.repository.JournalRepository

class JournalRepositoryImpl(
    private val preferences: JournalPreferences
) : JournalRepository {

    override fun stats(): AnglerStats = AnglerStats(
        totalXp = preferences.readInt(KEY_XP),
        sessions = preferences.readInt(KEY_SESSIONS),
        catches = preferences.readInt(KEY_CATCHES),
        heaviestGrams = preferences.readInt(KEY_HEAVIEST),
        streak = preferences.readInt(KEY_STREAK),
        lastSessionDay = preferences.readLong(KEY_LAST_DAY),
        gearScore = preferences.readInt(KEY_GEAR_SCORE),
        bestSpot = preferences.readString(KEY_BEST_SPOT).takeIf { it.isNotEmpty() }
            ?.let { name -> Spot.entries.firstOrNull { it.name == name } },
        claimedTiers = preferences.readString(KEY_TIERS)
            .split(SEPARATOR)
            .filter { it.isNotEmpty() }
            .mapNotNull { name -> RewardTier.entries.firstOrNull { it.name == name } }
            .toSet()
    )

    override fun saveStats(stats: AnglerStats) {
        preferences.writeInt(KEY_XP, stats.totalXp)
        preferences.writeInt(KEY_SESSIONS, stats.sessions)
        preferences.writeInt(KEY_CATCHES, stats.catches)
        preferences.writeInt(KEY_HEAVIEST, stats.heaviestGrams)
        preferences.writeInt(KEY_STREAK, stats.streak)
        preferences.writeLong(KEY_LAST_DAY, stats.lastSessionDay)
        preferences.writeInt(KEY_GEAR_SCORE, stats.gearScore)
        preferences.writeString(KEY_BEST_SPOT, stats.bestSpot?.name ?: "")
        preferences.writeString(KEY_TIERS, stats.claimedTiers.joinToString(SEPARATOR) { it.name })
    }

    override fun speciesProgress(): Map<SpeciesId, SpeciesProgress> =
        SpeciesId.entries.associateWith { id ->
            SpeciesProgress(
                id = id,
                count = preferences.readInt(speciesCountKey(id)),
                bestGrams = preferences.readInt(speciesBestKey(id))
            )
        }

    override fun saveSpeciesProgress(progress: Map<SpeciesId, SpeciesProgress>) {
        progress.values.forEach { entry ->
            preferences.writeInt(speciesCountKey(entry.id), entry.count)
            preferences.writeInt(speciesBestKey(entry.id), entry.bestGrams)
        }
    }

    override fun recentSessions(): List<SessionRecord> =
        preferences.readString(KEY_LOG)
            .split(RECORD_SEPARATOR)
            .filter { it.isNotEmpty() }
            .mapNotNull { decodeRecord(it) }

    override fun appendSession(record: SessionRecord) {
        val updated = (listOf(record) + recentSessions()).take(GameConfig.RECENT_SESSIONS)
        preferences.writeString(
            KEY_LOG,
            updated.joinToString(RECORD_SEPARATOR) { encodeRecord(it) }
        )
    }

    override fun dailyState(day: Long, kind: DailyChallengeKind): DailyState {
        val storedDay = preferences.readLong(KEY_DAILY_DAY, -1L)
        val progress = if (storedDay == day) preferences.readInt(KEY_DAILY_PROGRESS) else 0
        val claimed = preferences.readLong(KEY_DAILY_CLAIMED, -1L) == day
        return DailyState(kind = kind, progress = progress, claimed = claimed)
    }

    override fun saveDailyProgress(day: Long, progress: Int) {
        preferences.writeLong(KEY_DAILY_DAY, day)
        preferences.writeInt(KEY_DAILY_PROGRESS, progress)
    }

    override fun markDailyClaimed(day: Long) {
        preferences.writeLong(KEY_DAILY_CLAIMED, day)
    }

    override fun reset() {
        val keys = mutableListOf(
            KEY_XP, KEY_SESSIONS, KEY_CATCHES, KEY_HEAVIEST, KEY_STREAK, KEY_LAST_DAY,
            KEY_GEAR_SCORE, KEY_BEST_SPOT, KEY_TIERS, KEY_LOG,
            KEY_DAILY_DAY, KEY_DAILY_PROGRESS, KEY_DAILY_CLAIMED
        )
        SpeciesId.entries.forEach { id ->
            keys.add(speciesCountKey(id))
            keys.add(speciesBestKey(id))
        }
        preferences.clearProgress(keys)
    }

    private fun encodeRecord(record: SessionRecord): String = listOf(
        record.timestamp.toString(),
        record.spot.name,
        record.weather.name,
        record.catches.toString(),
        record.grams.toString(),
        record.xp.toString()
    ).joinToString(SEPARATOR)

    private fun decodeRecord(raw: String): SessionRecord? {
        val parts = raw.split(SEPARATOR)
        if (parts.size < 6) {
            return null
        }
        val spot = Spot.entries.firstOrNull { it.name == parts[1] } ?: return null
        val weather = Weather.entries.firstOrNull { it.name == parts[2] } ?: return null
        return SessionRecord(
            timestamp = parts[0].toLongOrNull() ?: return null,
            spot = spot,
            weather = weather,
            catches = parts[3].toIntOrNull() ?: 0,
            grams = parts[4].toIntOrNull() ?: 0,
            xp = parts[5].toIntOrNull() ?: 0
        )
    }

    private fun speciesCountKey(id: SpeciesId): String = "species_count_" + id.name

    private fun speciesBestKey(id: SpeciesId): String = "species_best_" + id.name

    private companion object {
        const val SEPARATOR = "|"
        const val RECORD_SEPARATOR = ";"
        const val KEY_XP = "total_xp"
        const val KEY_SESSIONS = "sessions"
        const val KEY_CATCHES = "catches"
        const val KEY_HEAVIEST = "heaviest_grams"
        const val KEY_STREAK = "streak"
        const val KEY_LAST_DAY = "last_session_day"
        const val KEY_GEAR_SCORE = "gear_score"
        const val KEY_BEST_SPOT = "best_spot"
        const val KEY_TIERS = "claimed_tiers"
        const val KEY_LOG = "session_log"
        const val KEY_DAILY_DAY = "daily_day"
        const val KEY_DAILY_PROGRESS = "daily_progress"
        const val KEY_DAILY_CLAIMED = "daily_claimed_day"
    }
}
