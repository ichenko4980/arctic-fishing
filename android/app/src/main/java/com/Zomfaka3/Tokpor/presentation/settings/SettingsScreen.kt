package com.Zomfaka3.Tokpor.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Zomfaka3.Tokpor.R
import com.Zomfaka3.Tokpor.core.di.ServiceLocator
import com.Zomfaka3.Tokpor.core.theme.DeepWater
import com.Zomfaka3.Tokpor.core.theme.FrostWhite
import com.Zomfaka3.Tokpor.core.theme.IceMint
import com.Zomfaka3.Tokpor.core.theme.TextMuted
import com.Zomfaka3.Tokpor.core.theme.TextPrimary
import com.Zomfaka3.Tokpor.core.theme.TrackIce
import com.Zomfaka3.Tokpor.core.theme.TrophyCoral
import com.Zomfaka3.Tokpor.core.ui.BentoCard
import com.Zomfaka3.Tokpor.core.ui.ScreenScaffold
import com.Zomfaka3.Tokpor.core.ui.SecondaryButton
import com.Zomfaka3.Tokpor.domain.model.UnitSystem
import com.Zomfaka3.Tokpor.presentation.dialog.ConfirmResetDialog

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val settingsViewModel: SettingsViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by settingsViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        settingsViewModel.refresh()
    }

    ScreenScaffold(
        title = stringResource(R.string.settings_title),
        backLabel = stringResource(R.string.cta_menu),
        onBack = onBack
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        SettingsCard(
            pounds = state.settings.unitSystem == UnitSystem.POUNDS,
            reduceMotion = state.settings.reduceMotion,
            onPounds = settingsViewModel::setPounds,
            onReduceMotion = settingsViewModel::setReduceMotion,
            onReset = settingsViewModel::askReset
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.settings_footer),
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        SecondaryButton(
            text = stringResource(R.string.cta_menu),
            onClick = onBack
        )
        Spacer(modifier = Modifier.height(18.dp))
    }

    if (state.confirmReset) {
        ConfirmResetDialog(
            onConfirm = settingsViewModel::confirmReset,
            onCancel = settingsViewModel::cancelReset
        )
    }
}

@Composable
private fun SettingsCard(
    pounds: Boolean,
    reduceMotion: Boolean,
    onPounds: (Boolean) -> Unit,
    onReduceMotion: (Boolean) -> Unit,
    onReset: () -> Unit
) {
    val onLabel = stringResource(R.string.state_on)
    val offLabel = stringResource(R.string.state_off)
    BentoCard(
        modifier = Modifier.fillMaxWidth(),
        accent = IceMint,
        padding = PaddingValues(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.settings_units),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Text(
                    text = if (pounds) {
                        stringResource(R.string.settings_units_lb)
                    } else {
                        stringResource(R.string.settings_units_kg)
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
            Switch(
                checked = pounds,
                onCheckedChange = onPounds,
                modifier = Modifier.semantics {
                    stateDescription = if (pounds) onLabel else offLabel
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = FrostWhite,
                    checkedTrackColor = DeepWater,
                    uncheckedThumbColor = DeepWater,
                    uncheckedTrackColor = TrackIce
                )
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.settings_reduce_motion),
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
            Switch(
                checked = reduceMotion,
                onCheckedChange = onReduceMotion,
                modifier = Modifier.semantics {
                    stateDescription = if (reduceMotion) onLabel else offLabel
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = FrostWhite,
                    checkedTrackColor = DeepWater,
                    uncheckedThumbColor = DeepWater,
                    uncheckedTrackColor = TrackIce
                )
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.settings_reset),
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
            OutlinedButton(
                onClick = onReset,
                modifier = Modifier.heightIn(min = 48.dp)
            ) {
                Text(
                    text = stringResource(R.string.cta_reset),
                    style = MaterialTheme.typography.labelLarge,
                    color = TrophyCoral
                )
            }
        }
    }
}
