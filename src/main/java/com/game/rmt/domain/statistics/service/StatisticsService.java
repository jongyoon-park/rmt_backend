package com.game.rmt.domain.statistics.service;

import com.game.rmt.domain.game.domain.Game;
import com.game.rmt.domain.game.service.GameService;
import com.game.rmt.domain.platform.domain.Platform;
import com.game.rmt.domain.platform.service.PlatformService;
import com.game.rmt.domain.statistics.dto.MonthlyEachGameResponse;
import com.game.rmt.domain.statistics.dto.MonthlyGameRequest;
import com.game.rmt.domain.statistics.dto.MonthlyPlatformRequest;
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
    private final PlatformService platformService;

    public MonthlyEachGameResponse getMonthlyGameStatics(MonthlyGameRequest request) {
        Game findGame = validateMonthlyGameStatics(request);
        List<MonthlyStaticsDTO> monthlyStatics = getMonthlyGameStaticsByCondition(request);

        return new MonthlyEachGameResponse(findGame.getName(), monthlyStatics);
    }

    public MonthlyEachGameResponse getMonthlyPlatformStatics(MonthlyPlatformRequest request) {
        Platform findPlatform = validateMonthlyPlatformStatics(request);
        List<MonthlyStaticsDTO> monthlyStatics = getMonthlyPlatformStaticsByCondition(request);

        return new MonthlyEachGameResponse(findPlatform.getName(), monthlyStatics);
    }

    private Platform validateMonthlyPlatformStatics(MonthlyPlatformRequest request) {
        request.isValidParam();
        return platformService.getPlatform(request.getPlatformId());
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
        return switch (request.getRangeDateCondition()) {
            case RANGE_DATE -> getMonthlyStaticsByGameIdBetweenDate(request);
            case ONLY_START_DATE -> getMonthlyStaticsByGameIdAndStartDate(request);
            case ONLY_END_DATE -> getMonthlyStaticsByGameIdAndEndDate(request);
            default -> staticsRepository.findMonthlyStaticsByGameId(request.getGameId());
        };
    }

    private List<MonthlyStaticsDTO> getMonthlyStaticsByPlatformId(MonthlyPlatformRequest request) {
        return staticsRepository.findMonthlyStaticsByPlatformId(request.getPlatformId());
    }

    private List<MonthlyStaticsDTO> getMonthlyStaticsByPlatformIdBetweenDate(MonthlyPlatformRequest request) {
        return staticsRepository.findMonthlyStaticsByPlatformIdBetweenDate(
                request.getPlatformId(),
                request.getStartDate(),
                request.getEndDate()
        );
    }

    private List<MonthlyStaticsDTO> getMonthlyStaticsByPlatformIdAndStartDate(MonthlyPlatformRequest request) {
        return staticsRepository.findMonthlyStaticsByPlatformIdAndStartDate(
                request.getPlatformId(),
                request.getStartDate()
        );
    }

    private List<MonthlyStaticsDTO> getMonthlyStaticsByPlatformIdAndEndDate(MonthlyPlatformRequest request) {
        return staticsRepository.findMonthlyStaticsByPlatformIdAndEndDate(
                request.getPlatformId(),
                request.getEndDate()
        );
    }

    private List<MonthlyStaticsDTO> getMonthlyPlatformStaticsByCondition(MonthlyPlatformRequest request) {
        return switch (request.getRangeDateCondition()) {
            case RANGE_DATE -> getMonthlyStaticsByPlatformIdBetweenDate(request);
            case ONLY_START_DATE -> getMonthlyStaticsByPlatformIdAndStartDate(request);
            case ONLY_END_DATE -> getMonthlyStaticsByPlatformIdAndEndDate(request);
            default -> staticsRepository.findMonthlyStaticsByPlatformId(request.getPlatformId());
        };
    }
}
