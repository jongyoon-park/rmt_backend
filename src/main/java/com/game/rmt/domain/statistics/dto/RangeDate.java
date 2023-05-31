package com.game.rmt.domain.statistics.dto;

import lombok.Getter;

@Getter
public enum RangeDate {
    RANGE_DATE("RANGE_DATE"),
    ONLY_START_DATE("ONLY_START_DATE"),
    ONLY_END_DATE("ONLY_END_DATE"),
    NONE("NONE");

    private final String rangeDateValue;

    RangeDate(String rangeDateValue) {
        this.rangeDateValue = rangeDateValue;
    }
}
