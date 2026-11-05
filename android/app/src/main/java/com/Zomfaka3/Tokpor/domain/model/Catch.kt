package com.Zomfaka3.Tokpor.domain.model

data class Catch(
    val species: SpeciesId,
    val grams: Int
)

data class CastOutcome(
    val bite: Boolean,
    val species: SpeciesId?,
    val grams: Int,
    val xp: Int
)
