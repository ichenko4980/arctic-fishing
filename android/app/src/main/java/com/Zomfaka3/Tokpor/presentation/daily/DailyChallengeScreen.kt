package com.Zomfaka3.Tokpor.presentation.daily

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.Zomfaka3.Tokpor.core.theme.RewardGold
import com.Zomfaka3.Tokpor.core.theme.SurfaceWhite
import com.Zomfaka3.Tokpor.core.theme.TextMuted
import com.Zomfaka3.Tokpor.core.theme.TextPrimary
import com.Zomfaka3.Tokpor.core.ui.BentoCard
import com.Zomfaka3.Tokpor.core.ui.PrimaryButton
import com.Zomfaka3.Tokpor.core.ui.ProgressBarRow
import com.Zomfaka3.Tokpor.core.ui.ScreenScaffold
import com.Zomfaka3.Tokpor.core.ui.SecondaryButton
import com.Zomfaka3.Tokpor.core.ui.dailyChallengeText

@Composable
fun DailyChallengeScreen(onBack: () -> Unit) {
    val dailyViewModel: DailyChallengeViewModel = viewModel(
        factory = ServiceLocator.viewModelFactory
    )
    val state by dailyViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        dailyViewModel.refresh()
    }

    ScreenScaffold(
        title = stringResource(R.string.daily_title),
        backLabel = stringResource(R.string.cta_menu),
        onBack = onBack
    ) {
        val daily = state.daily
        Spacer(modifier = Modifier.height(16.dp))
        BentoCard(
            modifier = Modifier.fillMaxWidth(),
            accent = RewardGold,
            cornerRadius = 24,
            padding = PaddingValues(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.ic_flame),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = RewardGold
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.daily_title),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if (daily != null) {
                    dailyChallengeText(daily.kind)
                } else {
                    stringResource(R.string.daily_not_started)
                },
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (daily != null) {
                ProgressBarRow(
                    label = stringResource(R.string.daily_progress),
                    valueText = stringResource(
                        R.string.progress_fraction,
                        daily.progress,
                        daily.kind.target
                    ),
                    progress = daily.fraction,
                    accent = DeepWater
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.daily_reward),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, RewardGold)
                ) {
                    Text(
                        text = stringResource(R.string.value_xp_gain, state.bonusXp),
                        style = MaterialTheme.typography.labelLarge,
                        color = RewardGold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        PrimaryButton(
            text = if (daily != null && daily.claimed) {
                stringResource(R.string.cta_claimed_today)
            } else {
                stringResource(R.string.cta_claim_reward)
            },
            onClick = { dailyViewModel.claim() },
            enabled = daily != null && daily.complete && !daily.claimed,
            iconRes = R.drawable.ic_check
        )
        Spacer(modifier = Modifier.height(10.dp))
        SecondaryButton(
            text = stringResource(R.string.cta_menu),
            onClick = onBack
        )
        Spacer(modifier = Modifier.height(18.dp))
    }
}
