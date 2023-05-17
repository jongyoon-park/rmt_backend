package com.game.rmt.domain.game.service;

import com.game.rmt.domain.game.domain.Game;
import com.game.rmt.domain.game.dto.GameDTO;
import com.game.rmt.domain.game.dto.GameSearchFilter;
import com.game.rmt.domain.game.repository.GameRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    public List<GameDTO> getGames(GameSearchFilter gameSearchFilter) {
        List<Game> gameList = gameRepository.findGames(gameSearchFilter);

        if (gameList == null || gameList.isEmpty()) {
            return new ArrayList<>();
        }

        return convertGameDTOList(gameList);
    }

    private List<GameDTO> convertGameDTOList(List<Game> gameList) {
        List<GameDTO> gameDTOList = new ArrayList<>();

        for (Game game : gameList) {
            gameDTOList.add(new GameDTO(game));
        }

        return gameDTOList;
    }
}
