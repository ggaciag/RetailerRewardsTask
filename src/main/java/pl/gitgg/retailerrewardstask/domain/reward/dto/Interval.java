package pl.gitgg.retailerrewardstask.domain.reward.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class Interval {

    private LocalDate from;
    private LocalDate to;
}
