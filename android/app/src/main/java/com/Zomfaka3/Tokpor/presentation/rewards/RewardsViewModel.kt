package com.Zomfaka3.Tokpor.presentation.rewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Zomfaka3.Tokpor.domain.model.RewardTier
import com.Zomfaka3.Tokpor.domain.model.RewardTierState
import com.Zomfaka3.Tokpor.domain.repository.JournalRepository
import com.Zomfaka3.Tokpor.domain.usecase.ClaimRewardUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RewardsUiState(
    val totalXp: Int = 0,
    val tiers: List<RewardTierState> = emptyList()
)

class RewardsViewModel(
    private val journal: JournalRepository,
    private val claimReward: ClaimRewardUseCase
) : ViewModel() {

    private val state = MutableStateFlow(RewardsUiState())

    val uiState: StateFlow<RewardsUiState> = state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val stats = journal.stats()
            state.update {
                it.copy(
                    totalXp = stats.totalXp,
                    tiers = RewardTier.entries.map { tier ->
                        RewardTierState(
                            tier = tier,
                            unlocked = stats.totalXp >= tier.requiredXp,
                            claimed = tier in stats.claimedTiers
                        )
                    }
                )
            }
        }
    }

    fun claim(tier: RewardTier) {
        viewModelScope.launch {
            claimReward(tier)
            refresh()
        }
    }
}
