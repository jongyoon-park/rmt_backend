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
    public MonthlyRatioDTO(String month, double currentPrice, double previousPrice) {
        this.month = month;
        this.percentage = calculatePercentage(currentPrice, previousPrice);
    }

    private double calculatePercentage(double currentPrice, double previousPrice) {
        return roundTwoDecimalPlaces(calculateRatio(currentPrice, previousPrice));
    }

    private double roundTwoDecimalPlaces(double ratio) {
        return Math.round(ratio * PERCENTILE_RANK) / PERCENTILE_RANK;
    }

    private double calculateRatio(double currentPrice, double previousPrice) {
        return (currentPrice - previousPrice) / previousPrice * PERCENTILE_RANK;
    }
}
