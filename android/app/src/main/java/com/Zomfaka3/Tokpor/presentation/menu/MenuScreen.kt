package com.Zomfaka3.Tokpor.presentation.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.Zomfaka3.Tokpor.core.theme.TextOnDeep
import com.Zomfaka3.Tokpor.core.theme.TextPrimary
import com.Zomfaka3.Tokpor.core.ui.AppBackground
import com.Zomfaka3.Tokpor.core.ui.BentoCard
import com.Zomfaka3.Tokpor.core.ui.LightFrostOverlay
import com.Zomfaka3.Tokpor.core.ui.SnowLayer
import com.Zomfaka3.Tokpor.core.ui.tierName

@Composable
fun MenuScreen(
    onStartSession: () -> Unit,
    onDaily: () -> Unit,
    onProgress: () -> Unit,
    onCollection: () -> Unit,
    onRewards: () -> Unit,
    onSettings: () -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by menuViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        menuViewModel.refresh()
    }

    MenuContent(
        state = state,
        onStartSession = onStartSession,
        onDaily = onDaily,
        onProgress = onProgress,
        onCollection = onCollection,
        onRewards = onRewards,
        onSettings = onSettings
    )
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onStartSession: () -> Unit,
    onDaily: () -> Unit,
    onProgress: () -> Unit,
    onCollection: () -> Unit,
    onRewards: () -> Unit,
    onSettings: () -> Unit
) {
    AppBackground(overlayColors = LightFrostOverlay) {
        SnowLayer(
            enabled = !state.reduceMotion,
            modifier = Modifier.fillMaxSize(),
            tint = SurfaceWhite
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp)
        ) {
            MenuHeader(level = state.level, totalXp = state.totalXp)
            StartTile(onStartSession = onStartSession)
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TileCard(
                    modifier = Modifier
                        .weight(1f)
                        .height(112.dp),
                    iconRes = R.drawable.ic_flame,
                    accent = RewardGold,
                    title = stringResource(R.string.menu_tile_daily),
                    value = dailyValue(state),
                    onClick = onDaily
                )
                TileCard(
                    modifier = Modifier
                        .weight(1f)
                        .height(112.dp),
                    iconRes = R.drawable.ic_chart,
                    accent = DeepWater,
                    title = stringResource(R.string.menu_tile_progress),
                    value = if (state.sessions > 0) {
                        stringResource(R.string.menu_sessions_value, state.sessions)
                    } else {
                        stringResource(R.string.menu_no_sessions)
                    },
                    onClick = onProgress
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TileCard(
                    modifier = Modifier
                        .weight(1f)
                        .height(112.dp),
                    spriteRes = R.drawable.sprite_1,
                    accent = DeepWater,
                    title = stringResource(R.string.menu_tile_collection),
                    value = if (state.speciesUnlocked > 0) {
                        stringResource(
                            R.string.menu_species_value,
                            state.speciesUnlocked,
                            state.speciesTotal
                        )
                    } else {
                        stringResource(R.string.menu_nothing_caught)
                    },
                    onClick = onCollection
                )
                TileCard(
                    modifier = Modifier
                        .weight(1f)
                        .height(112.dp),
                    spriteRes = R.drawable.sprite_5,
                    accent = RewardGold,
                    title = stringResource(R.string.menu_tile_rewards),
                    value = rewardsValue(state),
                    valueColor = if (state.rewardsReady > 0) RewardGold else TextPrimary,
                    onClick = onRewards
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            SettingsTile(onSettings = onSettings)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.menu_footer),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun SettingsTile(onSettings: () -> Unit) {
    BentoCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        accent = IceMint,
        padding = PaddingValues(horizontal = 16.dp),
        onClick = onSettings
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_settings),
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = DeepWater
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.menu_tile_settings),
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimary
            )
        }
    }
}

@Composable
private fun dailyValue(state: MenuUiState): String {
    val daily = state.daily ?: return stringResource(R.string.menu_daily_fresh)
    return stringResource(R.string.progress_fraction, daily.progress, daily.kind.target)
}

@Composable
private fun rewardsValue(state: MenuUiState): String {
    val next = state.nextTier
    return when {
        state.rewardsReady > 0 -> stringResource(R.string.menu_rewards_ready, state.rewardsReady)
        next != null -> stringResource(R.string.menu_rewards_next, tierName(next))
        else -> stringResource(R.string.menu_rewards_all)
    }
}

@Composable
private fun MenuHeader(level: Int, totalXp: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = stringResource(R.string.cd_logo),
            modifier = Modifier.size(40.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = stringResource(R.string.brand_title),
            style = MaterialTheme.typography.titleMedium,
            color = DeepNight,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = SurfaceWhite.copy(alpha = 0.9f),
            border = BorderStroke(1.dp, IceMint)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_badge),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = RewardGold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.menu_level_badge, level, totalXp),
                    style = MaterialTheme.typography.labelSmall,
                    color = DeepWater
                )
            }
        }
    }
}

@Composable
private fun StartTile(onStartSession: () -> Unit) {
    val shape = RoundedCornerShape(24.dp)
    Button(
        onClick = onStartSession,
        modifier = Modifier
            .fillMaxWidth()
            .height(148.dp),
        shape = shape,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = FrostWhite
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(listOf(DeepWater, DeepNight)),
                    shape = shape
                )
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_4),
                contentDescription = stringResource(R.string.cd_gear_pack),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 12.dp, bottom = 12.dp)
                    .size(84.dp),
                contentScale = ContentScale.Fit
            )
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 20.dp, end = 104.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.menu_tile_today),
                    style = MaterialTheme.typography.labelSmall,
                    color = IceMint
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.cta_start_session),
                    style = MaterialTheme.typography.headlineSmall,
                    color = FrostWhite
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.menu_tile_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextOnDeep
                )
            }
        }
    }
}

@Composable
private fun TileCard(
    modifier: Modifier,
    accent: Color,
    title: String,
    value: String,
    onClick: () -> Unit,
    iconRes: Int? = null,
    spriteRes: Int? = null,
    valueColor: Color = TextPrimary
) {
    BentoCard(modifier = modifier, accent = accent, onClick = onClick) {
        if (spriteRes != null) {
            Image(
                painter = painterResource(spriteRes),
                contentDescription = null,
                modifier = Modifier.size(42.dp),
                contentScale = ContentScale.Fit
            )
        } else if (iconRes != null) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(26.dp),
                tint = accent
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = valueColor,
            maxLines = 2
        )
    }
}
