package com.Zomfaka3.Tokpor.presentation.progress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Zomfaka3.Tokpor.R
import com.Zomfaka3.Tokpor.core.di.ServiceLocator
import com.Zomfaka3.Tokpor.core.theme.DeepWater
import com.Zomfaka3.Tokpor.core.theme.IceMint
import com.Zomfaka3.Tokpor.core.theme.RewardGold
import com.Zomfaka3.Tokpor.core.theme.SurfaceWhite
import com.Zomfaka3.Tokpor.core.theme.TextMuted
import com.Zomfaka3.Tokpor.core.theme.TextPrimary
import com.Zomfaka3.Tokpor.core.theme.TextSecondary
import com.Zomfaka3.Tokpor.core.theme.TrophyCoral
import com.Zomfaka3.Tokpor.core.ui.BentoCard
import com.Zomfaka3.Tokpor.core.ui.ProgressBarRow
import com.Zomfaka3.Tokpor.core.ui.ScreenScaffold
import com.Zomfaka3.Tokpor.core.ui.SecondaryButton
import com.Zomfaka3.Tokpor.core.ui.StatCard
import com.Zomfaka3.Tokpor.core.ui.spotName
import com.Zomfaka3.Tokpor.core.ui.tierName
import com.Zomfaka3.Tokpor.core.ui.weightText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val SessionDateFormat = SimpleDateFormat("dd MMM", Locale.US)

@Composable
fun ProgressScreen(onBack: () -> Unit) {
    val progressViewModel: ProgressViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by progressViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        progressViewModel.refresh()
    }

    ScreenScaffold(
        title = stringResource(R.string.progress_title),
        backLabel = stringResource(R.string.cta_menu),
        onBack = onBack
    ) {
        Spacer(modifier = Modifier.height(14.dp))
        BentoCard(
            modifier = Modifier.fillMaxWidth(),
            accent = DeepWater,
            padding = PaddingValues(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.progress_level, state.level),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                val title = state.title
                if (title != null) {
                    Text(
                        text = tierName(title),
                        style = MaterialTheme.typography.labelSmall,
                        color = RewardGold
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            ProgressBarRow(
                label = stringResource(R.string.value_xp, state.stats.totalXp),
                valueText = stringResource(
                    R.string.progress_level_xp,
                    state.xpInLevel,
                    state.xpPerLevel
                ),
                progress = state.levelFraction,
                accent = DeepWater
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
        StatGrid(state = state)
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = stringResource(R.string.progress_recent),
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(state.sessions) { record ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 56.dp),
                    color = SurfaceWhite.copy(alpha = 0.8f),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(
                                R.string.progress_row,
                                SessionDateFormat.format(Date(record.timestamp)),
                                spotName(record.spot)
                            ),
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextSecondary
                        )
                        Text(
                            text = stringResource(
                                R.string.progress_row,
                                record.catches.toString(),
                                weightText(record.grams, state.unitSystem)
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = DeepWater
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        SecondaryButton(
            text = stringResource(R.string.cta_menu),
            onClick = onBack
        )
        Spacer(modifier = Modifier.height(18.dp))
    }
}

@Composable
private fun StatGrid(state: ProgressUiState) {
    val stats = state.stats
    val cards = mutableListOf<Triple<String, String, androidx.compose.ui.graphics.Color>>()
    if (stats.sessions > 0) {
        cards.add(
            Triple(
                stringResource(R.string.stat_sessions),
                stringResource(R.string.value_plain, stats.sessions),
                DeepWater
            )
        )
    }
    if (stats.catches > 0) {
        cards.add(
            Triple(
                stringResource(R.string.stat_catches),
                stringResource(R.string.value_plain, stats.catches),
                TrophyCoral
            )
        )
    }
    if (stats.heaviestGrams > 0) {
        cards.add(
            Triple(
                stringResource(R.string.stat_heaviest),
                weightText(stats.heaviestGrams, state.unitSystem),
                RewardGold
            )
        )
    }
    if (stats.streak > 0) {
        cards.add(
            Triple(
                stringResource(R.string.stat_streak),
                stringResource(R.string.value_plain, stats.streak),
                IceMint
            )
        )
    }
    if (stats.gearScore > 0) {
        cards.add(
            Triple(
                stringResource(R.string.stat_gear_score),
                stringResource(R.string.value_plain, stats.gearScore),
                DeepWater
            )
        )
    }
    val bestSpot = stats.bestSpot
    if (bestSpot != null) {
        cards.add(
            Triple(
                stringResource(R.string.stat_best_spot),
                spotName(bestSpot),
                TrophyCoral
            )
        )
    }
    if (cards.size < 2) {
        BentoCard(
            modifier = Modifier.fillMaxWidth(),
            accent = IceMint,
            padding = PaddingValues(20.dp)
        ) {
            Text(
                text = stringResource(R.string.progress_empty),
                style = MaterialTheme.typography.bodyLarge,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        return
    }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        cards.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { entry ->
                    StatCard(
                        modifier = Modifier.weight(1f),
                        label = entry.first,
                        value = entry.second,
                        accent = entry.third
                    )
                }
                if (row.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
