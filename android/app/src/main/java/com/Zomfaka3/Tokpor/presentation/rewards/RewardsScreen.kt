package com.Zomfaka3.Tokpor.presentation.rewards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Zomfaka3.Tokpor.R
import com.Zomfaka3.Tokpor.core.di.ServiceLocator
import com.Zomfaka3.Tokpor.core.theme.DeepWater
import com.Zomfaka3.Tokpor.core.theme.FrostWhite
import com.Zomfaka3.Tokpor.core.theme.RewardGold
import com.Zomfaka3.Tokpor.core.theme.TextMuted
import com.Zomfaka3.Tokpor.core.theme.TextPrimary
import com.Zomfaka3.Tokpor.core.ui.BentoCard
import com.Zomfaka3.Tokpor.core.ui.ScreenScaffold
import com.Zomfaka3.Tokpor.core.ui.SecondaryButton
import com.Zomfaka3.Tokpor.core.ui.tierName
import com.Zomfaka3.Tokpor.domain.model.RewardTierState

@Composable
fun RewardsScreen(onBack: () -> Unit) {
    val rewardsViewModel: RewardsViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by rewardsViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        rewardsViewModel.refresh()
    }

    ScreenScaffold(
        title = stringResource(R.string.rewards_title),
        backLabel = stringResource(R.string.cta_menu),
        onBack = onBack
    ) {
        Spacer(modifier = Modifier.height(14.dp))
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(state.tiers) { entry ->
                TierRow(
                    entry = entry,
                    onClaim = { rewardsViewModel.claim(entry.tier) }
                )
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
private fun TierRow(
    entry: RewardTierState,
    onClaim: () -> Unit
) {
    val claimedLabel = stringResource(R.string.rewards_state_claimed)
    val lockedLabel = stringResource(R.string.rewards_state_locked)
    val readyLabel = stringResource(R.string.rewards_state_claim)
    BentoCard(
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                stateDescription = when {
                    entry.claimed -> claimedLabel
                    entry.unlocked -> readyLabel
                    else -> lockedLabel
                }
            },
        accent = if (entry.unlocked) RewardGold else TextMuted,
        padding = PaddingValues(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = stringResource(R.string.cd_badge),
                modifier = Modifier
                    .size(46.dp)
                    .alpha(if (entry.unlocked) 1f else 0.35f),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tierName(entry.tier),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (entry.unlocked) TextPrimary else TextMuted
                )
                Text(
                    text = stringResource(
                        R.string.rewards_requirement,
                        entry.tier.requiredXp
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            when {
                entry.claimed -> StatePill(
                    text = claimedLabel,
                    iconRes = R.drawable.ic_check
                )

                entry.unlocked -> Button(
                    onClick = onClaim,
                    modifier = Modifier.heightIn(min = 48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RewardGold,
                        contentColor = FrostWhite
                    )
                ) {
                    Text(
                        text = readyLabel,
                        style = MaterialTheme.typography.labelLarge,
                        color = FrostWhite
                    )
                }

                else -> StatePill(
                    text = lockedLabel,
                    iconRes = R.drawable.ic_lock
                )
            }
        }
    }
}

@Composable
private fun StatePill(text: String, iconRes: Int) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = FrostWhite,
        border = BorderStroke(1.dp, TextMuted)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = DeepWater
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }
    }
}
