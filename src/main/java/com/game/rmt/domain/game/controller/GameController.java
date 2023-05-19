package com.game.rmt.domain.game.controller;

import com.game.rmt.domain.game.dto.GameDTO;
import com.game.rmt.domain.game.dto.GameListResponse;
import com.game.rmt.domain.game.dto.GameSearchFilter;
import com.game.rmt.domain.game.dto.NewGameRequest;
import com.game.rmt.domain.game.service.GameService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("rmt/game")
public class GameController {

    private final GameService gameService;

    @GetMapping("/list")
    public GameListResponse getGames(@RequestBody GameSearchFilter gameSearchFilter) {
        return new GameListResponse(gameService.getGames(gameSearchFilter));
    }

    @PostMapping("")
    public GameDTO createGame(@RequestBody NewGameRequest newGameRequest) {
        return gameService.createGame(newGameRequest);
    }
}
