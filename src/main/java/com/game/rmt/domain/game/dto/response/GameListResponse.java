package com.game.rmt.domain.game.dto.response;

import com.game.rmt.domain.game.dto.GameDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameListResponse {
    private List<GameDTO> gameList;
}
