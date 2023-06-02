package com.game.rmt.domain.statistics.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameRatioDTO {

    private String gameName;
    private double percentage;

    private final double PERCENTILE_RANK = 100.0;
    private final double DECIMAL_MULTIPLE_VALUE = PERCENTILE_RANK * PERCENTILE_RANK;

    public GameRatioDTO(String gameName, double totalPrice, int price) {
        this.gameName = gameName;
        this.percentage = calculatePercentage(totalPrice, price);
    }

    private double calculatePercentage(double totalPrice, int price) {
        return roundTwoDecimalPlaces(totalPrice, price);
    }

    private double roundTwoDecimalPlaces(double totalPrice, int price) {
        return Math.round(((double) price / totalPrice * DECIMAL_MULTIPLE_VALUE)) / PERCENTILE_RANK;
    }
}
