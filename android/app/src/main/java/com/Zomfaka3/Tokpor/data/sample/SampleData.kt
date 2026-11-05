package com.Zomfaka3.Tokpor.data.sample

import com.Zomfaka3.Tokpor.domain.model.DailyChallengeKind
import com.Zomfaka3.Tokpor.domain.model.GearItem
import com.Zomfaka3.Tokpor.domain.model.RewardTier
import com.Zomfaka3.Tokpor.domain.model.Species
import com.Zomfaka3.Tokpor.domain.model.SpeciesId
import com.Zomfaka3.Tokpor.domain.model.Spot
import com.Zomfaka3.Tokpor.domain.model.Weather

object SampleData {

    val species: List<Species> = listOf(
        Species(SpeciesId.ICE_PERCH, drawWeight = 44, minGrams = 300, maxGrams = 1100, xp = 8),
        Species(SpeciesId.ARCTIC_CHAR, drawWeight = 30, minGrams = 1000, maxGrams = 3600, xp = 14),
        Species(SpeciesId.NORTHERN_PIKE, drawWeight = 18, minGrams = 2200, maxGrams = 6400, xp = 22),
        Species(SpeciesId.BURBOT, drawWeight = 8, minGrams = 3500, maxGrams = 9800, xp = 40)
    )

    val spots: List<Spot> = listOf(Spot.FROZEN_BAY, Spot.DEEP_CRACK, Spot.NORTH_SHELF)

    val weather: List<Weather> = listOf(Weather.CLEAR, Weather.SNOW, Weather.BLIZZARD)

    val gear: List<GearItem> = listOf(
        GearItem.ICE_AUGER,
        GearItem.THERMOS,
        GearItem.SHORT_ROD,
        GearItem.LURE_BOX,
        GearItem.SLED
    )

    val tiers: List<RewardTier> = listOf(
        RewardTier.ICE_SCOUT,
        RewardTier.FROST_ANGLER,
        RewardTier.DEEP_DRILLER,
        RewardTier.POLAR_MASTER
    )

    val challenges: List<DailyChallengeKind> = listOf(
        DailyChallengeKind.LOG_THREE_CATCHES,
        DailyChallengeKind.CHECK_ALL_GEAR,
        DailyChallengeKind.CATCH_TROPHY,
        DailyChallengeKind.FINISH_IN_BLIZZARD
    )

    val speciesXp: Map<SpeciesId, Int> = species.associate { it.id to it.xp }
}
