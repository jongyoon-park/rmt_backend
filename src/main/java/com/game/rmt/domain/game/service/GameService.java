package com.game.rmt.domain.game.service;

import com.game.rmt.domain.game.domain.Game;
import com.game.rmt.domain.game.dto.GameDTO;
import com.game.rmt.domain.game.dto.request.GameSearchFilter;
import com.game.rmt.domain.game.dto.request.NewGameRequest;
import com.game.rmt.domain.game.repository.GameRepository;
import com.game.rmt.domain.platform.domain.Platform;
import com.game.rmt.domain.platform.service.PlatformService;
import com.game.rmt.global.errorhandler.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.game.rmt.global.errorhandler.exception.ErrorCode.NOT_FOUND_GAME;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final PlatformService platformService;

    public List<GameDTO> getGames(GameSearchFilter gameSearchFilter) {
        List<Game> gameList = gameRepository.findGames(gameSearchFilter);

        if (gameList == null || gameList.isEmpty()) {
            return new ArrayList<>();
        }

        return convertGameDTOList(gameList);
    }

    public GameDTO createGame(NewGameRequest newGameRequest) {
        newGameRequest.isValidParam();
        Platform findPlatform = platformService.getPlatform(newGameRequest.getPlatformId());
        Game newGame = new Game(newGameRequest.getGameName(), findPlatform);

        return new GameDTO(gameRepository.save(newGame));
    }

    public Game getGame(long id) {
        Game game = gameRepository.findGameById(id);

        if (game == null) {
            throw new NotFoundException(NOT_FOUND_GAME);
        }

        return game;
    }

    private List<GameDTO> convertGameDTOList(List<Game> gameList) {
        List<GameDTO> gameDTOList = new ArrayList<>();

        for (Game game : gameList) {
            gameDTOList.add(new GameDTO(game));
        }

        return gameDTOList;
    }
}
