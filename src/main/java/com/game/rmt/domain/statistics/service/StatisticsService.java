package com.game.rmt.domain.statistics.service;

import com.game.rmt.domain.game.domain.Game;
import com.game.rmt.domain.game.service.GameService;
import com.game.rmt.domain.statistics.dto.MonthlyEachGameResponse;
import com.game.rmt.domain.statistics.dto.MonthlyGameRequest;
import com.game.rmt.domain.statistics.dto.MonthlyStaticsDTO;
import com.game.rmt.domain.statistics.repository.custom.StaticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StaticsRepository staticsRepository;

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
        return staticsRepository.findMonthlyStaticsByGameIdBetweenDate(
                request.getGameId(),
                request.getStartDate(),
                request.getEndDate()
        );
    }

    private List<MonthlyStaticsDTO> getMonthlyStaticsByGameIdAndStartDate(MonthlyGameRequest request) {
        return staticsRepository.findMonthlyStaticsByGameIdAndStartDate(
                request.getGameId(),
                request.getStartDate()
        );
    }

    private List<MonthlyStaticsDTO> getMonthlyStaticsByGameIdAndEndDate(MonthlyGameRequest request) {
        return staticsRepository.findMonthlyStaticsByGameIdAndEndDate(
                request.getGameId(),
                request.getEndDate()
        );
    }

    private List<MonthlyStaticsDTO> getMonthlyGameStaticsByCondition(MonthlyGameRequest request) {
        switch (request.getRangeDateCondition()) {
            case RANGE_DATE :
                return getMonthlyStaticsByGameIdBetweenDate(request);
            case ONLY_START_DATE:
                return getMonthlyStaticsByGameIdAndStartDate(request);
            case ONLY_END_DATE:
                return getMonthlyStaticsByGameIdAndEndDate(request);
            default:
                return staticsRepository.findMonthlyStaticsByGameId(request.getGameId());
        }
    }
}
