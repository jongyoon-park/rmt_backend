package com.game.rmt.domain.statistics.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GameTotalPriceDTO {
    private String gameName;
    private Integer price;

    @QueryProjection
    public GameTotalPriceDTO(String gameName, Integer price) {
        this.gameName = gameName;
        this.price = price;
    }
}
