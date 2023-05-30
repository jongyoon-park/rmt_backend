package com.game.rmt.domain.statistics.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MonthlyStaticsDTO {
    private String month;
    private Integer staticValue;

    @QueryProjection
    public MonthlyStaticsDTO(String month, Integer staticValue) {
        this.month = month;
        this.staticValue = staticValue;
    }
}
