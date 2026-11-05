package com.Zomfaka3.Tokpor.presentation.collection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Zomfaka3.Tokpor.R
import com.Zomfaka3.Tokpor.core.di.ServiceLocator
import com.Zomfaka3.Tokpor.core.theme.DeepWater
import com.Zomfaka3.Tokpor.core.theme.TextPrimary
import com.Zomfaka3.Tokpor.core.ui.ProgressBarRow
import com.Zomfaka3.Tokpor.core.ui.ScreenScaffold
import com.Zomfaka3.Tokpor.core.ui.SecondaryButton
import com.Zomfaka3.Tokpor.core.ui.SpeciesCard
import com.Zomfaka3.Tokpor.core.ui.speciesName
import com.Zomfaka3.Tokpor.core.ui.spriteOf
import com.Zomfaka3.Tokpor.core.ui.weightText

@Composable
fun CollectionScreen(onBack: () -> Unit) {
    val collectionViewModel: CollectionViewModel = viewModel(
        factory = ServiceLocator.viewModelFactory
    )
    val state by collectionViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        collectionViewModel.refresh()
    }

    ScreenScaffold(
        title = stringResource(R.string.collection_title),
        backLabel = stringResource(R.string.cta_menu),
        onBack = onBack
    ) {
        Spacer(modifier = Modifier.height(14.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.ic_fish),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = DeepWater
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.collection_title),
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimary
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        ProgressBarRow(
            label = stringResource(R.string.daily_progress),
            valueText = stringResource(
                R.string.collection_counter,
                state.unlocked,
                state.entries.size
            ),
            progress = if (state.entries.isEmpty()) {
                0f
            } else {
                state.unlocked.toFloat() / state.entries.size.toFloat()
            },
            accent = DeepWater
        )
        Spacer(modifier = Modifier.height(14.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            state.entries.chunked(2).forEach { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    pair.forEach { entry ->
                        SpeciesCard(
                            modifier = Modifier.weight(1f),
                            name = speciesName(entry.id),
                            drawableRes = spriteOf(entry.id),
                            unlocked = entry.unlocked,
                            bestText = stringResource(
                                R.string.collection_best,
                                weightText(entry.bestGrams, state.unitSystem)
                            ),
                            countText = stringResource(
                                R.string.collection_count,
                                entry.count
                            ),
                            lockedText = stringResource(R.string.collection_locked),
                            accent = DeepWater
                        )
                    }
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
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
