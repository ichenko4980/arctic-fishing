package com.Zomfaka3.Tokpor.domain.usecase

import com.Zomfaka3.Tokpor.domain.model.CastOutcome
import com.Zomfaka3.Tokpor.domain.model.Species
import com.Zomfaka3.Tokpor.domain.model.SpeciesId
import com.Zomfaka3.Tokpor.domain.model.Spot
import com.Zomfaka3.Tokpor.domain.model.Weather
import kotlin.math.roundToInt
import kotlin.random.Random

class ResolveCastUseCase(
    private val catalogue: List<Species>,
    private val baseBite: Float,
    private val gearBonus: Float
) {

    operator fun invoke(
        spot: Spot,
        weather: Weather,
        gearChecked: Int,
        random: Random
    ): CastOutcome {
        if (random.nextFloat() > biteChance(spot, weather, gearChecked)) {
            return CastOutcome(bite = false, species = null, grams = 0, xp = 0)
        }
        val weights = catalogue.map { drawWeightFor(it, spot) }
        var roll = random.nextInt(weights.sum())
        var index = 0
        while (index < weights.size - 1 && roll >= weights[index]) {
            roll -= weights[index]
            index++
        }
        val species = catalogue[index]
        val span = species.maxGrams - species.minGrams
        val raw = species.minGrams + random.nextInt(span + 1)
        val grams = ((raw * spot.massMultiplier) / 100f).roundToInt() * 100
        return CastOutcome(bite = true, species = species.id, grams = grams, xp = species.xp)
    }

    fun biteChance(spot: Spot, weather: Weather, gearChecked: Int): Float =
        (baseBite + gearBonus * gearChecked + spot.biteBonus + weather.biteBonus)
            .coerceIn(MIN_CHANCE, MAX_CHANCE)

    private fun drawWeightFor(species: Species, spot: Spot): Int = when {
        spot.trophyShift == 0 -> species.drawWeight
        species.id == SpeciesId.BURBOT -> species.drawWeight + spot.trophyShift
        species.id == SpeciesId.ICE_PERCH -> species.drawWeight - spot.trophyShift
        else -> species.drawWeight
    }

    private companion object {
        const val MIN_CHANCE = 0.15f
        const val MAX_CHANCE = 0.92f
    }
}
