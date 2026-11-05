package com.Zomfaka3.Tokpor.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.Zomfaka3.Tokpor.R
import com.Zomfaka3.Tokpor.core.config.GameConfig
import com.Zomfaka3.Tokpor.domain.model.DailyChallengeKind
import com.Zomfaka3.Tokpor.domain.model.GearItem
import com.Zomfaka3.Tokpor.domain.model.RewardTier
import com.Zomfaka3.Tokpor.domain.model.SpeciesId
import com.Zomfaka3.Tokpor.domain.model.Spot
import com.Zomfaka3.Tokpor.domain.model.UnitSystem
import com.Zomfaka3.Tokpor.domain.model.Weather
import java.util.Locale

fun spriteOf(id: SpeciesId): Int = when (id) {
    SpeciesId.ARCTIC_CHAR -> R.drawable.sprite_1
    SpeciesId.NORTHERN_PIKE -> R.drawable.sprite_2
    SpeciesId.ICE_PERCH -> R.drawable.sprite_3
    SpeciesId.BURBOT -> R.drawable.sprite_4
}

@Composable
fun speciesName(id: SpeciesId): String = stringResource(
    when (id) {
        SpeciesId.ICE_PERCH -> R.string.species_ice_perch
        SpeciesId.ARCTIC_CHAR -> R.string.species_arctic_char
        SpeciesId.NORTHERN_PIKE -> R.string.species_northern_pike
        SpeciesId.BURBOT -> R.string.species_burbot
    }
)

@Composable
fun spotName(spot: Spot): String = stringResource(
    when (spot) {
        Spot.FROZEN_BAY -> R.string.spot_frozen_bay
        Spot.DEEP_CRACK -> R.string.spot_deep_crack
        Spot.NORTH_SHELF -> R.string.spot_north_shelf
    }
)

@Composable
fun weatherName(weather: Weather): String = stringResource(
    when (weather) {
        Weather.CLEAR -> R.string.weather_clear
        Weather.SNOW -> R.string.weather_snow
        Weather.BLIZZARD -> R.string.weather_blizzard
    }
)

@Composable
fun gearName(item: GearItem): String = stringResource(
    when (item) {
        GearItem.ICE_AUGER -> R.string.gear_ice_auger
        GearItem.THERMOS -> R.string.gear_thermos
        GearItem.SHORT_ROD -> R.string.gear_short_rod
        GearItem.LURE_BOX -> R.string.gear_lure_box
        GearItem.SLED -> R.string.gear_sled
    }
)

@Composable
fun tierName(tier: RewardTier): String = stringResource(
    when (tier) {
        RewardTier.ICE_SCOUT -> R.string.tier_ice_scout
        RewardTier.FROST_ANGLER -> R.string.tier_frost_angler
        RewardTier.DEEP_DRILLER -> R.string.tier_deep_driller
        RewardTier.POLAR_MASTER -> R.string.tier_polar_master
    }
)

@Composable
fun dailyChallengeText(kind: DailyChallengeKind): String = stringResource(
    when (kind) {
        DailyChallengeKind.LOG_THREE_CATCHES -> R.string.daily_log_three
        DailyChallengeKind.CHECK_ALL_GEAR -> R.string.daily_all_gear
        DailyChallengeKind.CATCH_TROPHY -> R.string.daily_trophy
        DailyChallengeKind.FINISH_IN_BLIZZARD -> R.string.daily_blizzard
    }
)

@Composable
fun weightText(grams: Int, unitSystem: UnitSystem): String {
    val amount = if (unitSystem == UnitSystem.POUNDS) {
        grams * GameConfig.POUNDS_PER_GRAM
    } else {
        grams / GameConfig.GRAMS_PER_KILO
    }
    val formatted = String.format(Locale.US, "%.1f", amount)
    return if (unitSystem == UnitSystem.POUNDS) {
        stringResource(R.string.weight_lb, formatted)
    } else {
        stringResource(R.string.weight_kg, formatted)
    }
}
