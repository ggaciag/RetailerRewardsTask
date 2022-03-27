package pl.gitgg.retailerrewardstask.domain.reward;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gitgg.retailerrewardstask.domain.reward.dto.Aggregation;
import pl.gitgg.retailerrewardstask.domain.reward.dto.Reward;
import pl.gitgg.retailerrewardstask.domain.transaction.TransactionService;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final TransactionService transactionService;

    public Reward calculateReward(String userId, Aggregation aggregation) {
        return null;
    }
}
