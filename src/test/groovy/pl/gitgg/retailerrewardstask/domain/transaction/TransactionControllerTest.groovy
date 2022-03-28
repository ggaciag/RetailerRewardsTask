package pl.gitgg.retailerrewardstask.domain.transaction


import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.time.LocalDate

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class TransactionControllerTest extends Specification {

    private static final URL = "/api/transaction"

    MockMvc mockMvc;

    TransactionRepository repository = Mock()
    TransactionService transactionService = new TransactionService(repository)

    def setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TransactionController(transactionService)).build()
    }


    def "can get all transactions"() {
        setup:
        repository.findAll() >> [
                new Transaction("1", "dave", LocalDate.of(2022, 1, 1), new BigDecimal(100)),
                new Transaction("2", "frank", LocalDate.of(2022, 1, 1), new BigDecimal(200))
        ]
        expect:
        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$[0].id').value("1"))
                .andExpect(jsonPath('$[0].date').value([2022, 1, 1]))
                .andExpect(jsonPath('$[0].userId').value("dave"))
                .andExpect(jsonPath('$[0].value').value("100"))
                .andExpect(jsonPath('$[1].id').value("2"))
                .andExpect(jsonPath('$[1].date').value([2022, 1, 1]))
                .andExpect(jsonPath('$[1].userId').value("frank"))
                .andExpect(jsonPath('$[1].value').value("200"))
    }

    def "can get single transaction"() {

        setup:
        repository.findById("1") >> Optional.of(new Transaction("1", "dave", LocalDate.of(2022, 1, 1), new BigDecimal(100)))

        expect:
        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.id').value("1"))
                .andExpect(jsonPath('$.date').value([2022, 1, 1]))
                .andExpect(jsonPath('$.userId').value("dave"))
                .andExpect(jsonPath('$.value').value("100"))
    }

    def "not found where transaction doesn't exist"() {
        setup:
        repository.findById("1") >> Optional.empty()

        expect:
        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isNotFound())
    }

    def "can delete transaction"() {
        setup:
        repository.existsById("1") >> true
        when:
        mockMvc.perform(delete(URL + "/1"))
                .andExpect(status().isNoContent())
        then:
        1 * repository.deleteById("1");
    }

    def "not found while deleting non existing transaction"() {
        setup:
        repository.existsById("1") >> false
        when:
        mockMvc.perform(delete(URL + "/1"))
        then:
        0 * repository.deleteById("1");
    }

    def "can create transaction when all data are passed"() {
        when:
        mockMvc.perform(post(URL).content(transactionJson()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
        then:
        repository.insert(_) >> {
            Transaction transaction = it[0]
            assert transaction.getId().size() == 36
            assert transaction.getDate() == LocalDate.of(2022,1,1)
            assert transaction.value == 100
            assert transaction.userId == "dave"
            return transaction
        }
    }

    def "can create transaction when only required fields are provided"() {
        when:
        mockMvc.perform(post(URL).content(transactionNoDateAndIdJson()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
        then:
        repository.insert(_) >> {
            Transaction transaction = it[0]
            assert transaction.getId().size() == 36
            assert transaction.getDate() == LocalDate.now()
            assert transaction.value == 100
            assert transaction.userId == "dave"
            return transaction
        }
    }

    def "bad request when creating transaction with negative value"() {
        when:
        mockMvc.perform(post(URL)
                .content(transactionNegativeValueJson()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
        then:
        0 * repository.insert(_);

    }

    def "bad request when creating transaction without userId"() {
        when:
        mockMvc.perform(post(URL)
                .content(transactionNoUserIdJson()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
        then:
        0 * repository.insert(_);

    }


    def "can update transaction"() {
        setup:
        repository.existsById("1") >> true
        when:
        mockMvc.perform(put(URL + "/1").content(transactionJson()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        then:
        repository.save(_) >> {
            Transaction transaction = it[0]
            assert transaction.getId() == "1"
            assert transaction.getDate() == LocalDate.of(2022, 1, 1)
            assert transaction.value == 100
            assert transaction.userId == "dave"
            return transaction
        }
    }

    def "can update transaction when no id adn date is provided inside body"() {
        setup:
        repository.existsById("1") >> true

        when:
        mockMvc.perform(put(URL + "/1").content(transactionNoDateAndIdJson()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        then:
        repository.save(_) >> {
            Transaction transaction = it[0]
            assert transaction.getId() == "1"
            assert transaction.getDate() == LocalDate.now()
            assert transaction.value == 100
            assert transaction.userId == "dave"
            return transaction
        }
    }

    def "bad request when updating transaction when id in url is diffrent then in body"() {
        setup:
        repository.existsById("2") >> true
        repository.existsById("1") >> true

        when:
        mockMvc.perform(put(URL + "/2").content(transactionNoUserIdJson()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
        then:
        0 * repository.save(_)

    }


    String transactionJson() {
        '''
            {
            "id": "1",
            "userId": "dave",
            "date": "2022-01-01",
            "value": 100
            }
        '''
    }

    String transactionNegativeValueJson() {
        '''
            {
            "id": "1",
            "userId": "dave",
            "date": "2022-01-01",
            "value": -100
            }
        '''
    }

    String transactionNoUserIdJson() {
        '''
            {
            "id": "1",         
            "date": "2022-01-01",
            "value": 100
            } 
        '''
    }

    String transactionNoDateAndIdJson() {
        '''
            {
            "userId": "dave",         
            "value": 100
            }
        '''
    }
}
