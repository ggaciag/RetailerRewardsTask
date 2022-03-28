package pl.gitgg.retailerrewardstask.domain.reward;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.gitgg.retailerrewardstask.domain.reward.dto.Aggregation;
import pl.gitgg.retailerrewardstask.domain.common.Interval;
import pl.gitgg.retailerrewardstask.domain.reward.dto.Reward;
import pl.gitgg.retailerrewardstask.domain.transaction.TransactionDto;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reward")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;

    @GetMapping("/{userId}")
    @ApiOperation(value = "Perform points calculation for given user and given time period")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Points calculated and returned", response = Reward.class),
            @ApiResponse(code = 400, message = "Wrong parameters")
    })
    public List<Reward> calculateRewardForUser(
            @PathVariable String userId,
            @ApiParam("Date from when transactions have to be taken into calculations, inclusive, format yyyy-mm-dd") @RequestParam(value = "from", required = false) LocalDate from,
            @ApiParam("Date until when transactions have to be taken into calculations, inclusive, format yyyy-mm-dd") @RequestParam(value = "to", required = false) LocalDate to,
            @ApiParam("Method of aggregation of calculated points. ALL - all points in total, MONTHLY - points returned for each month") @RequestParam("aggregation") Aggregation aggregation) {

        return rewardService.calculateReward(userId, new Interval(from, to), aggregation);
    }

}
