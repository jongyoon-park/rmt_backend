package com.game.rmt.domain.statistics.service;

import com.game.rmt.domain.account.repository.AccountRepository;
import com.game.rmt.domain.game.domain.Game;
import com.game.rmt.domain.game.service.GameService;
import com.game.rmt.domain.statistics.dto.MonthlyEachGameResponse;
import com.game.rmt.domain.statistics.dto.MonthlyGameRequest;
import com.game.rmt.domain.statistics.dto.MonthlyStaticsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final AccountRepository accountRepository;

    private final GameService gameService;

    public MonthlyEachGameResponse getMonthlyGameStatics(MonthlyGameRequest request) {
        Game findGame = validateMonthlyGameStatics(request);
        List<MonthlyStaticsDTO> monthlyStatics = getMonthlyGameStaticsByCondition(request);

        return new MonthlyEachGameResponse(findGame.getName(), monthlyStatics);
    }

    private Game validateMonthlyGameStatics(MonthlyGameRequest request) {
        request.isValidParam();
        return gameService.getGame(request.getGameId());
    }

    private List<MonthlyStaticsDTO> getMonthlyStaticsByGameIdBetweenDate(MonthlyGameRequest request) {
        return accountRepository.getMonthlyStaticsByGameIdBetweenDate(
                request.getGameId(),
                request.getStartDate(),
                request.getEndDate()
        );
    }

    private List<MonthlyStaticsDTO> getMonthlyStaticsByGameIdAndStartDate(MonthlyGameRequest request) {
        return accountRepository.getMonthlyStaticsByGameIdAndStartDate(
                request.getGameId(),
                request.getStartDate()
        );
    }

    private List<MonthlyStaticsDTO> getMonthlyStaticsByGameIdAndEndDate(MonthlyGameRequest request) {
        return accountRepository.getMonthlyStaticsByGameIdAndEndDate(
                request.getGameId(),
                request.getEndDate()
        );
    }

    private List<MonthlyStaticsDTO> getMonthlyGameStaticsByCondition(MonthlyGameRequest request) {
        if (request.isValidRangeDate()) {
            return getMonthlyStaticsByGameIdBetweenDate(request);
        }

        if (request.isOnlyStartDate()) {
            return getMonthlyStaticsByGameIdAndStartDate(request);
        }

        if (request.isOnlyEndDate()) {
            return getMonthlyStaticsByGameIdAndEndDate(request);
        }

        return accountRepository.getMonthlyStaticsByGameId(request.getGameId());
    }
}
