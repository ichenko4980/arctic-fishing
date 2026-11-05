package com.Zomfaka3.Tokpor.presentation.session

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Zomfaka3.Tokpor.R
import com.Zomfaka3.Tokpor.core.config.GameConfig
import com.Zomfaka3.Tokpor.core.di.ServiceLocator
import com.Zomfaka3.Tokpor.core.theme.BorderIce
import com.Zomfaka3.Tokpor.core.theme.DeepNight
import com.Zomfaka3.Tokpor.core.theme.DeepWater
import com.Zomfaka3.Tokpor.core.theme.FrostWhite
import com.Zomfaka3.Tokpor.core.theme.IceMint
import com.Zomfaka3.Tokpor.core.theme.PaleIce
import com.Zomfaka3.Tokpor.core.theme.RewardGold
import com.Zomfaka3.Tokpor.core.theme.SurfaceWhite
import com.Zomfaka3.Tokpor.core.theme.TextMuted
import com.Zomfaka3.Tokpor.core.theme.TextPrimary
import com.Zomfaka3.Tokpor.core.theme.TextSecondary
import com.Zomfaka3.Tokpor.core.theme.TrophyCoral
import com.Zomfaka3.Tokpor.core.ui.AppBackground
import com.Zomfaka3.Tokpor.core.ui.BentoCard
import com.Zomfaka3.Tokpor.core.ui.ChipRow
import com.Zomfaka3.Tokpor.core.ui.LightFrostOverlay
import com.Zomfaka3.Tokpor.core.ui.PrimaryButton
import com.Zomfaka3.Tokpor.core.ui.SecondaryButton
import com.Zomfaka3.Tokpor.core.ui.StatCard
import com.Zomfaka3.Tokpor.core.ui.gearName
import com.Zomfaka3.Tokpor.core.ui.speciesName
import com.Zomfaka3.Tokpor.core.ui.spotName
import com.Zomfaka3.Tokpor.core.ui.spriteOf
import com.Zomfaka3.Tokpor.core.ui.weatherName
import com.Zomfaka3.Tokpor.core.ui.weightText
import com.Zomfaka3.Tokpor.domain.model.SessionSummary
import com.Zomfaka3.Tokpor.presentation.dialog.PauseDialog

@Composable
fun SessionScreen(
    onMenu: () -> Unit,
    onFinished: (SessionSummary) -> Unit
) {
    val sessionViewModel: SessionViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by sessionViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        sessionViewModel.prepare()
    }
    LaunchedEffect(Unit) {
        sessionViewModel.events.collect { summary ->
            onFinished(summary)
        }
    }
    DisposableEffect(Unit) {
        onDispose { sessionViewModel.pause() }
    }

    SessionContent(
        state = state,
        onMenu = onMenu,
        onPause = { sessionViewModel.setPaused(true) },
        onResume = { sessionViewModel.setPaused(false) },
        onSelectSpot = sessionViewModel::selectSpot,
        onSelectWeather = sessionViewModel::selectWeather,
        onToggleGear = sessionViewModel::toggleGear,
        onDropLine = sessionViewModel::dropLine,
        onEndSession = sessionViewModel::endSession
    )
}

@Composable
private fun SessionContent(
    state: SessionUiState,
    onMenu: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onSelectSpot: (Int) -> Unit,
    onSelectWeather: (Int) -> Unit,
    onToggleGear: (com.Zomfaka3.Tokpor.domain.model.GearItem) -> Unit,
    onDropLine: () -> Unit,
    onEndSession: () -> Unit
) {
    AppBackground(overlayColors = LightFrostOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            SessionHeader(
                spotLabel = spotName(state.spot),
                onMenu = onMenu,
                onPause = onPause
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (state.phase == SessionPhase.SETUP) {
                    SetupPanel(
                        state = state,
                        onSelectSpot = onSelectSpot,
                        onSelectWeather = onSelectWeather,
                        onToggleGear = onToggleGear
                    )
                } else {
                    FishingPanel(state = state)
                }
                CatchReveal(state = state)
            }
            StatStrip(state = state)
            ControlsPanel(
                state = state,
                onDropLine = onDropLine,
                onEndSession = onEndSession
            )
        }
    }
    if (state.paused) {
        PauseDialog(
            onResume = onResume,
            onEndSession = onEndSession
        )
    }
}

@Composable
private fun SessionHeader(
    spotLabel: String,
    onMenu: () -> Unit,
    onPause: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = SurfaceWhite.copy(alpha = 0.78f),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .heightIn(min = 48.dp)
                    .clickable(role = Role.Button, onClick = onMenu),
                shape = RoundedCornerShape(14.dp),
                color = Color.Transparent
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_back),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = DeepWater
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.cta_menu),
                        style = MaterialTheme.typography.labelLarge,
                        color = DeepWater
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.session_label),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
                Text(
                    text = spotLabel,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Surface(
                modifier = Modifier
                    .size(48.dp)
                    .clickable(role = Role.Button, onClick = onPause),
                shape = CircleShape,
                color = SurfaceWhite.copy(alpha = 0.9f),
                border = BorderStroke(1.dp, BorderIce)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(R.drawable.icon_pause),
                        contentDescription = stringResource(R.string.cd_pause),
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

@Composable
private fun SetupPanel(
    state: SessionUiState,
    onSelectSpot: (Int) -> Unit,
    onSelectWeather: (Int) -> Unit,
    onToggleGear: (com.Zomfaka3.Tokpor.domain.model.GearItem) -> Unit
) {
    val selectedLabel = stringResource(R.string.state_selected)
    val unselectedLabel = stringResource(R.string.state_not_selected)
    val checkedLabel = stringResource(R.string.state_checked)
    val uncheckedLabel = stringResource(R.string.state_unchecked)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = stringResource(R.string.session_spot_label),
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
        )
        Spacer(modifier = Modifier.height(6.dp))
        ChipRow(
            options = state.spots.map { spotName(it) },
            selectedIndex = state.spotIndex,
            selectedLabel = selectedLabel,
            unselectedLabel = unselectedLabel,
            onSelect = onSelectSpot
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.ic_thermometer),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = DeepWater
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.session_weather_label),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        ChipRow(
            options = state.weatherOptions.map { weatherName(it) },
            selectedIndex = state.weatherIndex,
            selectedLabel = selectedLabel,
            unselectedLabel = unselectedLabel,
            onSelect = onSelectWeather
        )
        Spacer(modifier = Modifier.height(14.dp))
        BentoCard(
            modifier = Modifier.fillMaxWidth(),
            accent = IceMint,
            padding = PaddingValues(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.session_gear_label),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
                Text(
                    text = stringResource(
                        R.string.session_gear_count,
                        state.gearChecked,
                        state.gearOptions.size
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = DeepWater
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            state.gearOptions.forEach { item ->
                val checked = item in state.checkedGear
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .semantics {
                            stateDescription = if (checked) checkedLabel else uncheckedLabel
                        }
                        .clickable(role = Role.Checkbox) { onToggleGear(item) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(28.dp),
                        shape = RoundedCornerShape(9.dp),
                        color = if (checked) DeepWater else SurfaceWhite,
                        border = BorderStroke(1.dp, if (checked) DeepWater else BorderIce)
                    ) {
                        if (checked) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_check),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = FrostWhite
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = gearName(item),
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (checked) TextPrimary else TextSecondary
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.session_bite_chance, state.biteChancePercent),
            style = MaterialTheme.typography.labelSmall,
            color = DeepWater,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { liveRegion = LiveRegionMode.Polite },
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FishingPanel(state: SessionUiState) {
    val transition = rememberInfiniteTransition()
    val breathe by transition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val holeScale = if (state.reduceMotion) 1f else breathe
    val eventText = when {
        state.phase == SessionPhase.CASTING -> stringResource(R.string.session_event_casting)
        state.lastOutcome == null -> stringResource(R.string.session_event_ready)
        state.lastOutcome.bite -> stringResource(R.string.session_event_bite)
        else -> stringResource(R.string.session_event_miss)
    }
    val eventColor = when {
        state.phase == SessionPhase.CASTING -> DeepWater
        state.lastOutcome?.bite == true -> RewardGold
        state.lastOutcome != null -> TextMuted
        else -> TextSecondary
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = eventText,
            style = MaterialTheme.typography.headlineMedium,
            color = eventColor,
            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .size(214.dp)
                .scale(holeScale)
                .border(width = 3.dp, color = PaleIce, shape = CircleShape)
                .background(
                    brush = Brush.radialGradient(listOf(DeepNight, DeepWater, IceMint)),
                    shape = CircleShape
                )
        )
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = stringResource(R.string.session_catch_strip),
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            for (slot in 0 until GameConfig.CATCH_SLOTS) {
                val logged = state.catches.getOrNull(slot)
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .border(width = 1.dp, color = BorderIce, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (logged != null) {
                        Image(
                            painter = painterResource(spriteOf(logged.species)),
                            contentDescription = speciesName(logged.species),
                            modifier = Modifier.size(28.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CatchReveal(state: SessionUiState) {
    val outcome = state.lastOutcome
    val species = outcome?.species
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = state.revealVisible && species != null,
            enter = scaleIn(
                animationSpec = spring(
                    dampingRatio = 0.55f,
                    stiffness = Spring.StiffnessMedium
                ),
                initialScale = 0.7f
            ) + fadeIn(animationSpec = tween(durationMillis = 180)),
            exit = scaleOut(animationSpec = tween(durationMillis = 180)) +
                fadeOut(animationSpec = tween(durationMillis = 180))
        ) {
            if (species != null) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = SurfaceWhite.copy(alpha = 0.95f),
                    border = BorderStroke(2.dp, RewardGold),
                    shadowElevation = 10.dp
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 26.dp, vertical = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(spriteOf(species)),
                            contentDescription = speciesName(species),
                            modifier = Modifier.size(118.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = speciesName(species),
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = weightText(outcome?.grams ?: 0, state.unitSystem),
                            style = MaterialTheme.typography.headlineMedium,
                            color = RewardGold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatStrip(state: SessionUiState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .semantics { liveRegion = LiveRegionMode.Polite },
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.stat_casts),
            value = stringResource(
                R.string.value_fraction,
                state.castsUsed,
                GameConfig.CASTS_PER_SESSION
            ),
            accent = DeepWater
        )
        StatCard(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.stat_catches),
            value = stringResource(R.string.value_plain, state.catches.size),
            accent = TrophyCoral
        )
        StatCard(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.stat_weight),
            value = weightText(state.totalGrams, state.unitSystem),
            accent = RewardGold
        )
    }
}

@Composable
private fun ControlsPanel(
    state: SessionUiState,
    onDropLine: () -> Unit,
    onEndSession: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        color = SurfaceWhite.copy(alpha = 0.92f),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.session_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            PrimaryButton(
                text = stringResource(R.string.cta_drop_line),
                onClick = onDropLine,
                enabled = state.phase != SessionPhase.CASTING && state.castsLeft > 0,
                iconRes = R.drawable.ic_play
            )
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryButton(
                text = stringResource(R.string.cta_end_session),
                onClick = onEndSession,
                iconRes = R.drawable.ic_check
            )
        }
    }
}
