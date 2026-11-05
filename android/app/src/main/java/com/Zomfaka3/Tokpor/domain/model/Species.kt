package com.Zomfaka3.Tokpor.domain.model

enum class SpeciesId {
    ICE_PERCH,
    ARCTIC_CHAR,
    NORTHERN_PIKE,
    BURBOT
}

data class Species(
    val id: SpeciesId,
    val drawWeight: Int,
    val minGrams: Int,
    val maxGrams: Int,
    val xp: Int
)

data class SpeciesProgress(
    val id: SpeciesId,
    val count: Int,
    val bestGrams: Int
) {
    val unlocked: Boolean
        get() = count > 0
}
