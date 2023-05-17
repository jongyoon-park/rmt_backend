package com.game.rmt.domain.game.dto;

import com.game.rmt.domain.game.domain.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameDTO {

    private Long gameId;
    private String gameName;
    private String platformName;

    public GameDTO (Game game) {
        this.gameId = game.getId();
        this.gameName = game.getName();
        this.platformName = game.getPlatform().getName();
    }
}
