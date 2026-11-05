package com.Zomfaka3.Tokpor.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Zomfaka3.Tokpor.R
import com.Zomfaka3.Tokpor.core.theme.DeepWater
import com.Zomfaka3.Tokpor.core.theme.FrostWhite
import com.Zomfaka3.Tokpor.core.theme.SurfaceWhite
import com.Zomfaka3.Tokpor.core.theme.TextPrimary

val LightFrostOverlay: List<Color> = listOf(
    FrostWhite.copy(alpha = 0.18f),
    FrostWhite.copy(alpha = 0.74f),
    FrostWhite.copy(alpha = 0.96f)
)

@Composable
fun ScreenScaffold(
    title: String,
    backLabel: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    overlayColors: List<Color> = LightFrostOverlay,
    content: @Composable ColumnScope.() -> Unit
) {
    AppBackground(overlayColors = overlayColors, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = SurfaceWhite.copy(alpha = 0.76f),
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .padding(horizontal = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onBack,
                        modifier = Modifier.heightIn(min = 48.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.icon_home),
                            contentDescription = stringResource(R.string.cd_home),
                            modifier = Modifier.size(20.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = backLabel,
                            style = MaterialTheme.typography.labelLarge,
                            color = DeepWater
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                content = content
            )
        }
    }
}
