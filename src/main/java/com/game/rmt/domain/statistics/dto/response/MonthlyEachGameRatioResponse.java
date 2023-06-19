package com.game.rmt.domain.statistics.dto.response;

import com.game.rmt.domain.statistics.dto.MonthlyRatioDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyEachGameRatioResponse {
    private String gameName;
    private List<MonthlyRatioDTO> monthlyRatioList;
}
