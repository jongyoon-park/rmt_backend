package com.game.rmt.domain.statistics.dto.response;

import com.game.rmt.domain.statistics.dto.GameRatioDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameRatioEachPlatformResponse {
    private List<String> platformNames;
    private List<GameRatioDTO> gameRatioList;
}
