package pl.gitgg.retailerrewardstask.domain.reward;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.gitgg.retailerrewardstask.domain.reward.dto.Aggregation;
import pl.gitgg.retailerrewardstask.domain.reward.dto.Reward;

@RestController
@RequestMapping("/api/reward")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;

    @GetMapping("/{userId}")
    public Reward calculateRewardForUser(
            @PathVariable String userId,
            @RequestParam("aggregation") Aggregation aggregation) {

        return rewardService.calculateReward(userId, aggregation);
    }

}
