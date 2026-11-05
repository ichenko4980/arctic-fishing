package com.Zomfaka3.Tokpor.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Zomfaka3.Tokpor.core.theme.DeepWater
import com.Zomfaka3.Tokpor.core.theme.TextMuted
import com.Zomfaka3.Tokpor.core.theme.TextPrimary

@Composable
fun SpeciesCard(
    name: String,
    drawableRes: Int,
    unlocked: Boolean,
    bestText: String,
    countText: String,
    lockedText: String,
    accent: Color,
    modifier: Modifier = Modifier,
    spriteSize: Int = 92
) {
    BentoCard(modifier = modifier, accent = if (unlocked) accent else TextMuted) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(drawableRes),
                contentDescription = if (unlocked) name else null,
                modifier = Modifier
                    .size(spriteSize.dp)
                    .alpha(if (unlocked) 1f else 0.2f),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.labelSmall,
                color = if (unlocked) TextPrimary else TextMuted,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (unlocked) {
                Text(
                    text = bestText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = DeepWater,
                    maxLines = 1
                )
                Text(
                    text = countText,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    maxLines = 1
                )
            } else {
                Text(
                    text = lockedText,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )
            }
        }
    }
}
