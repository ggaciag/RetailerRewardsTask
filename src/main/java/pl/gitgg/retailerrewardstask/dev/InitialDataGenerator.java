package pl.gitgg.retailerrewardstask.dev;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import pl.gitgg.retailerrewardstask.domain.transaction.Transaction;
import pl.gitgg.retailerrewardstask.domain.transaction.TransactionRepository;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Profile({"devEmbedded", "dev"})
public class InitialDataGenerator {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("classpath:data/transactions.json")
    Resource transactionsResource;

    @PostConstruct
    public void createTestData() {

        transactionRepository.deleteAll();

        transactionRepository.saveAll(prepareTransactions());

    }

    @SneakyThrows
    private List<Transaction> prepareTransactions() {
        return objectMapper.readValue(transactionsResource.getFile(), new TypeReference<List<Transaction>>() {});

    }

}
