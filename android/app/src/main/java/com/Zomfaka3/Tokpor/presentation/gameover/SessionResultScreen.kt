package com.Zomfaka3.Tokpor.presentation.gameover

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Zomfaka3.Tokpor.R
import com.Zomfaka3.Tokpor.core.di.ServiceLocator
import com.Zomfaka3.Tokpor.core.theme.DeepNight
import com.Zomfaka3.Tokpor.core.theme.DeepWater
import com.Zomfaka3.Tokpor.core.theme.FrostWhite
import com.Zomfaka3.Tokpor.core.theme.IceMint
import com.Zomfaka3.Tokpor.core.theme.RewardGold
import com.Zomfaka3.Tokpor.core.theme.SurfaceWhite
import com.Zomfaka3.Tokpor.core.theme.TextMuted
import com.Zomfaka3.Tokpor.core.theme.TextPrimary
import com.Zomfaka3.Tokpor.core.theme.TrophyCoral
import com.Zomfaka3.Tokpor.core.ui.AppBackground
import com.Zomfaka3.Tokpor.core.ui.PrimaryButton
import com.Zomfaka3.Tokpor.core.ui.ProgressBarRow
import com.Zomfaka3.Tokpor.core.ui.SecondaryButton
import com.Zomfaka3.Tokpor.core.ui.StatCard
import com.Zomfaka3.Tokpor.core.ui.speciesName
import com.Zomfaka3.Tokpor.core.ui.spriteOf
import com.Zomfaka3.Tokpor.core.ui.tierName
import com.Zomfaka3.Tokpor.core.ui.weightText
import com.Zomfaka3.Tokpor.domain.model.SessionSummary

@Composable
fun SessionResultScreen(
    summary: SessionSummary,
    onLogAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val resultViewModel: SessionResultViewModel = viewModel(
        factory = ServiceLocator.viewModelFactory
    )
    val state by resultViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        resultViewModel.refresh()
    }

    ResultContent(
        summary = summary,
        state = state,
        onLogAgain = onLogAgain,
        onMenu = onMenu
    )
}

@Composable
private fun ResultContent(
    summary: SessionSummary,
    state: SessionResultUiState,
    onLogAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val animatedCatches by animateIntAsState(
        targetValue = summary.catches,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
    )
    val animatedXp by animateIntAsState(
        targetValue = summary.xp,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
    )
    val hero = summary.bestSpecies
    AppBackground(
        overlayColors = listOf(
            DeepNight.copy(alpha = 0.82f),
            DeepWater.copy(alpha = 0.86f),
            FrostWhite.copy(alpha = 0.95f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = stringResource(R.string.result_label),
                style = MaterialTheme.typography.labelSmall,
                color = IceMint
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (summary.catches >= 2) {
                    stringResource(R.string.result_good)
                } else {
                    stringResource(R.string.result_quiet)
                },
                style = MaterialTheme.typography.displaySmall,
                color = FrostWhite,
                textAlign = TextAlign.Center,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
            Spacer(modifier = Modifier.height(14.dp))
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .background(
                            Brush.radialGradient(
                                listOf(IceMint.copy(alpha = 0.3f), Color.Transparent)
                            )
                        )
                )
                Image(
                    painter = painterResource(
                        if (hero != null) spriteOf(hero) else R.drawable.sprite_4
                    ),
                    contentDescription = if (hero != null) speciesName(hero) else null,
                    modifier = Modifier
                        .size(124.dp)
                        .alpha(if (hero != null) 1f else 0.45f),
                    contentScale = ContentScale.Fit
                )
            }
            if (hero == null) {
                Text(
                    text = stringResource(R.string.result_no_catch),
                    style = MaterialTheme.typography.bodyLarge,
                    color = FrostWhite,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.stat_catches),
                    value = stringResource(R.string.value_plain, animatedCatches),
                    accent = DeepWater
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.stat_total_weight),
                    value = weightText(summary.grams, state.unitSystem),
                    accent = TrophyCoral
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.stat_best_fish),
                    value = if (hero != null) {
                        weightText(summary.bestGrams, state.unitSystem)
                    } else {
                        stringResource(R.string.value_none)
                    },
                    accent = DeepWater
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.stat_xp_earned),
                    value = stringResource(R.string.value_xp_gain, animatedXp),
                    accent = RewardGold
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            BadgeStrip(summary = summary)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = SurfaceWhite.copy(alpha = 0.88f),
                border = BorderStroke(1.dp, IceMint)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    ProgressBarRow(
                        label = stringResource(R.string.progress_level, state.level),
                        valueText = stringResource(
                            R.string.progress_level_xp,
                            state.xpInLevel,
                            state.xpPerLevel
                        ),
                        progress = state.levelFraction,
                        accent = DeepWater
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            PrimaryButton(
                text = stringResource(R.string.cta_log_again),
                onClick = onLogAgain,
                iconRes = R.drawable.ic_play
            )
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryButton(
                text = stringResource(R.string.cta_menu),
                onClick = onMenu
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun BadgeStrip(summary: SessionSummary) {
    val tier = summary.unlockedTier ?: return
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(18.dp),
        color = SurfaceWhite.copy(alpha = 0.92f),
        border = BorderStroke(2.dp, RewardGold)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = stringResource(R.string.cd_badge),
                modifier = Modifier.size(40.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = stringResource(R.string.result_badge_unlocked),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
                Text(
                    text = tierName(tier),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
            }
        }
    }
}
