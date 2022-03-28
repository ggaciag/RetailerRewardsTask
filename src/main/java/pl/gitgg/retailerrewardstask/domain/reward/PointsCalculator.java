package pl.gitgg.retailerrewardstask.domain.reward;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class PointsCalculator {

    private static final BigDecimal FIRST_THRESHOLD = new BigDecimal(50);
    private static final BigDecimal FIRST_PRIZE = new BigDecimal(1);
    private static final BigDecimal SECOND_THRESHOLD = new BigDecimal(100);
    private static final BigDecimal SECOND_PRIZE = new BigDecimal(2);

    private static final BigDecimal MAX_POINTS_FOR_FIRST_THRESHOLD = SECOND_THRESHOLD.subtract(FIRST_THRESHOLD).multiply(FIRST_PRIZE);

    public Long calculate(List<BigDecimal> transactionValues) {

        Long overFirstThreshold = transactionValues.stream()
                .map(it -> it.setScale(0, RoundingMode.FLOOR))
                .map(it -> it.subtract(FIRST_THRESHOLD))
                .filter(it -> it.compareTo(BigDecimal.ZERO) > 0)
                .map(it -> it.multiply(FIRST_PRIZE))
                .map(MAX_POINTS_FOR_FIRST_THRESHOLD::min)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .longValue();


        Long overSecondThreshold = transactionValues.stream()
                .map(it -> it.setScale(0, RoundingMode.FLOOR))
                .map(it -> it.subtract(SECOND_THRESHOLD))
                .filter(it -> it.compareTo(BigDecimal.ZERO) > 0)
                .map(it -> it.multiply(SECOND_PRIZE))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .longValue();

        return overFirstThreshold + overSecondThreshold;
    }
}
