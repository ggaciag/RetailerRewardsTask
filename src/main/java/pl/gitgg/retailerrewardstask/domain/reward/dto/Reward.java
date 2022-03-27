package pl.gitgg.retailerrewardstask.domain.reward.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Reward {

    private String userId;
    private Long points;
    private Interval interval;

}
