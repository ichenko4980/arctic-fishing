package com.Zomfaka3.Tokpor.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ArcticColorScheme = lightColorScheme(
    primary = DeepWater,
    onPrimary = FrostWhite,
    primaryContainer = DeepNight,
    onPrimaryContainer = FrostWhite,
    secondary = IceMint,
    onSecondary = DeepNight,
    secondaryContainer = PaleIce,
    onSecondaryContainer = DeepNight,
    tertiary = RewardGold,
    onTertiary = DeepNight,
    tertiaryContainer = TrackIce,
    onTertiaryContainer = TextSecondary,
    background = FrostWhite,
    onBackground = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = TrackIce,
    onSurfaceVariant = TextSecondary,
    outline = BorderIce,
    outlineVariant = IceMint,
    error = TrophyCoral,
    onError = FrostWhite,
    scrim = AbyssBlue
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ArcticColorScheme,
        typography = AppTypography,
        content = content
    )
}
