package com.game.rmt.domain.game.repository.custom;

import com.game.rmt.domain.game.domain.Game;
import com.game.rmt.domain.game.dto.GameSearchFilter;

import java.util.List;

public interface GameRepositoryCustom {
    List<Game> findGames(GameSearchFilter gameSearchFilter);
    Game findGameById(Long gameId);
}
