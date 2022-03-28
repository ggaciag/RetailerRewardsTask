package pl.gitgg.retailerrewardstask.domain.transaction

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.time.LocalDate

@SpringBootTest
class TransactionRepositoryTest extends Specification {

    @Autowired
    TransactionRepository transactionRepository

    private static final USER_ID = "dave";

    def setup(){
        transactionRepository.deleteAll();
    }

    def "can find all transactions by user in selected date range"() {

        setup:
        transactionRepository.saveAll(prepareTransactions());
        when:
        List<Transaction> transactions = transactionRepository.findInTheForUserInThePeriod(
                USER_ID,
                LocalDate.of(2022, 1, 10),
                LocalDate.of(2022, 4, 10))
        then:
        transactions.size() == 3
        transactions.find {it.getId() == "2"} != null
        transactions.find {it.getId() == "3"} != null
        transactions.find {it.getId() == "4"} != null
    }

    def "can find all transactions by user in selected range inclusive"() {
        setup:
        transactionRepository.saveAll(prepareTransactions());
        when:
        List<Transaction> transactions = transactionRepository.findInTheForUserInThePeriod(
                USER_ID,
                LocalDate.of(2022, 1, 5),
                LocalDate.of(2022, 2, 6))
        then:
        transactions.size() == 2
        transactions.find {it.getId() == "1"} != null
        transactions.find {it.getId() == "2"} != null
    }

    List<Transaction> prepareTransactions() {
        [
                new Transaction("1", USER_ID, LocalDate.of(2022, 1, 5), new BigDecimal(100)),
                new Transaction("2", USER_ID, LocalDate.of(2022, 2, 6), new BigDecimal(200)),
                new Transaction("3", USER_ID, LocalDate.of(2022, 3, 7), new BigDecimal(300)),
                new Transaction("4", USER_ID, LocalDate.of(2022, 4, 8), new BigDecimal(400)),
                new Transaction("5", USER_ID, LocalDate.of(2022, 5, 9), new BigDecimal(500)),
                new Transaction("6", "frank", LocalDate.of(2022, 01, 05), new BigDecimal(100)),
        ]
    }
}
