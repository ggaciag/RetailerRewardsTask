package pl.gitgg.retailerrewardstask

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.gitgg.retailerrewardstask.domain.transaction.TransactionController
import spock.lang.Specification

@SpringBootTest
class LoadContextTest extends Specification {

    @Autowired
    private TransactionController transactionController;

    def "When context is loaded, beans are created"() {
        expect: "Controller is created"
        transactionController
    }

}
