package com.Zomfaka3.Tokpor.domain.usecase

import com.Zomfaka3.Tokpor.domain.model.AnglerStats
import com.Zomfaka3.Tokpor.domain.model.RewardTier
import com.Zomfaka3.Tokpor.domain.repository.JournalRepository

class ClaimRewardUseCase(private val repository: JournalRepository) {

    operator fun invoke(tier: RewardTier): AnglerStats {
        val stats = repository.stats()
        if (stats.totalXp < tier.requiredXp || tier in stats.claimedTiers) {
            return stats
        }
        val updated = stats.copy(claimedTiers = stats.claimedTiers + tier)
        repository.saveStats(updated)
        return updated
    }
}
