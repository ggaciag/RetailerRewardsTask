package pl.gitgg.retailerrewardstask.domain.reward.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.YearMonth;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Reward {

    private String userId;
    private Long points;
    private YearMonth yearMonth;
}
