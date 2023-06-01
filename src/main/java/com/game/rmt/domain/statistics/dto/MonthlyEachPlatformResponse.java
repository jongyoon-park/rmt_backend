package com.game.rmt.domain.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyEachPlatformResponse {
    private String platformName;
    private List<MonthlyStaticsDTO> monthlyStaticsList;
}
