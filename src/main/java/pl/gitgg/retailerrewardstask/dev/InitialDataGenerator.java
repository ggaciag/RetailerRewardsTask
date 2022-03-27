package pl.gitgg.retailerrewardstask.dev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.gitgg.retailerrewardstask.domain.transaction.Transaction;
import pl.gitgg.retailerrewardstask.domain.transaction.TransactionRepository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Component
@Profile("devEmbedded")
public class InitialDataGenerator {

    @Autowired
    private TransactionRepository transactionRepository;

    @PostConstruct
    public void createTestData() {

        transactionRepository.deleteAll();

        transactionRepository.saveAll(prepareTransactions());

    }

    private List<Transaction> prepareTransactions() {
        return Stream.of(
            Transaction.builder()
                    .id(UUID.randomUUID().toString())
                    .value(new BigDecimal("100.45"))
                    .build()
        ).toList();
    }

}
