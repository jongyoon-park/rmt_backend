package com.game.rmt.domain.statistics.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EachGameRatioResponse {
    private String gameName;
    private Long percentage;
}
