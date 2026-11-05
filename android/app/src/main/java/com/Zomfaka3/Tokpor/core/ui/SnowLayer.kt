package com.Zomfaka3.Tokpor.core.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import kotlin.math.sin

@Composable
fun SnowLayer(
    enabled: Boolean,
    modifier: Modifier = Modifier,
    tint: Color = Color.White,
    dots: Int = 14
) {
    if (!enabled) {
        return
    }
    val transition = rememberInfiniteTransition()
    val drift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        for (index in 0 until dots) {
            val seed = index * 0.137f
            val phase = (drift + seed) % 1f
            val x = ((seed * 7.3f) % 1f) * width + sin((phase + seed) * 6.28f) * 14f
            val y = phase * height
            val radius = 2.2f + (index % 4) * 1.1f
            val alpha = 0.25f + 0.35f * (1f - phase)
            drawCircle(
                color = tint.copy(alpha = alpha),
                radius = radius,
                center = Offset(x, y)
            )
        }
    }
}
