package com.game.rmt.domain.game.dto;

import com.game.rmt.domain.game.domain.Game;
import com.game.rmt.domain.platform.dto.PlatformDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameDTO {

    private Long gameId;
    private String gameName;
    private PlatformDTO platform;

    public GameDTO (Game game) {
        this.gameId = game.getId();
        this.gameName = game.getName();
        this.platform = new PlatformDTO(game.getPlatform());
    }
}
