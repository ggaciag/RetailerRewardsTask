package pl.gitgg.retailerrewardstask.domain.reward

import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import pl.gitgg.retailerrewardstask.domain.reward.dto.Aggregation
import pl.gitgg.retailerrewardstask.domain.transaction.Transaction
import pl.gitgg.retailerrewardstask.domain.transaction.TransactionService
import spock.lang.Specification

import java.time.LocalDate

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class RewardControllerTest extends Specification {


    private static final USER_ID = "dave"
    private static final URL = "/api/reward/" + USER_ID


    MockMvc mockMvc;

    TransactionService transactionService = Mock()
    PointsCalculator pointsCalculator = Mock()
    RewardService rewardService = new RewardService(transactionService, pointsCalculator)

    def setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new RewardController(rewardService)).build()
    }

    def "can have all points calculated"(){
        setup:
        transactionService.getForUserForInterval("dave", _) >>
                [new Transaction("1", "dave", LocalDate.of(2022, 1, 1), new BigDecimal(120))]
        pointsCalculator.calculate([new BigDecimal(120)]) >> 90

        expect:
        mockMvc.perform(get(URL).queryParam("aggregation", Aggregation.ALL.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath('$[0].points').value(90))
        .andExpect(jsonPath('$[0].userId').value("dave"))
    }

    def "can have all points calculated divided by months"(){
        setup:
        transactionService.getForUserForInterval("dave", _) >>
                [new Transaction("1", "dave", LocalDate.of(2022, 1, 1), new BigDecimal(120)),
                 new Transaction("2", "dave", LocalDate.of(2022, 2, 1), new BigDecimal(200))]
        pointsCalculator.calculate([new BigDecimal(120)]) >> 90
        pointsCalculator.calculate([new BigDecimal(200)]) >> 250

        expect:
        mockMvc.perform(get(URL).queryParam("aggregation", Aggregation.MONTHLY.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$[1].points').value(90))
                .andExpect(jsonPath('$[1].userId').value("dave"))
                .andExpect(jsonPath('$[0].points').value(250))
                .andExpect(jsonPath('$[0].userId').value("dave"))
    }

    def "bad request when wrong aggregation provided"(){
        setup:
        transactionService.getForUserForInterval("dave", _) >>
                [new Transaction("1", "dave", LocalDate.of(2022, 1, 1), new BigDecimal(120))]
        pointsCalculator.calculate([new BigDecimal(120)]) >> 90

        expect:
        mockMvc.perform(get(URL).queryParam("aggregation", "wrongAggregation"))
                .andExpect(status().isBadRequest())
    }
}
