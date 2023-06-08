package com.game.rmt.domain.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MonthlyRatioDTO {

    private String month;
    private double percentage;

    private final double PERCENTILE_RANK = 100.0;

    // 생성 시 증감 비율 계산해서 넣도록 변경하기
}
