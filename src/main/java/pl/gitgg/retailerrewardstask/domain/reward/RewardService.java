package pl.gitgg.retailerrewardstask.domain.reward;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gitgg.retailerrewardstask.domain.common.Interval;
import pl.gitgg.retailerrewardstask.domain.reward.dto.Aggregation;
import pl.gitgg.retailerrewardstask.domain.reward.dto.Reward;
import pl.gitgg.retailerrewardstask.domain.transaction.Transaction;
import pl.gitgg.retailerrewardstask.domain.transaction.TransactionService;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final TransactionService transactionService;
    private final PointsCalculator pointsCalculator;

    public List<Reward> calculateReward(String userId, Interval interval, Aggregation aggregation) {
        List<Transaction> transactions = transactionService.getForUserForInterval(userId, interval);

        List<Reward> rewards = new ArrayList<>();
        switch (aggregation) {
            case ALL -> rewards.add(calculateReward(userId, transactions, null));
            case MONTHLY -> rewards = groupAndCalculate(userId, transactions);
        }
        return rewards;
    }

    private List<Reward> groupAndCalculate(String userId, List<Transaction> transactions) {

        Map<YearMonth, List<Transaction>> groupedTransactions = transactions.stream()
                .collect(groupingBy(it -> YearMonth.from(it.getDate())));

        return groupedTransactions.entrySet().stream()
                .map(it -> calculateReward(userId, it.getValue(), it.getKey()))
                .toList();
    }

    private Reward calculateReward(String userId, List<Transaction> transactions, YearMonth yearMonth) {
        Long points = pointsCalculator.calculate(
                transactions.stream()
                        .map(Transaction::getValue)
                        .toList());

        return Reward.builder()
                .points(points)
                .userId(userId)
                .yearMonth(yearMonth)
                .build();

    }


}
