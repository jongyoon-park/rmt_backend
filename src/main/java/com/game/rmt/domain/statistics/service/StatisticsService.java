package com.game.rmt.domain.statistics.service;

import com.game.rmt.domain.game.domain.Game;
import com.game.rmt.domain.game.service.GameService;
import com.game.rmt.domain.platform.domain.Platform;
import com.game.rmt.domain.platform.service.PlatformService;
import com.game.rmt.domain.statistics.dto.GamePriceDTO;
import com.game.rmt.domain.statistics.dto.GameRatioDTO;
import com.game.rmt.domain.statistics.dto.MonthlyRatioDTO;
import com.game.rmt.domain.statistics.dto.request.GameRatioEachPlatformRequest;
import com.game.rmt.domain.statistics.dto.response.GameRatioEachPlatformResponse;
import com.game.rmt.domain.statistics.dto.response.MonthlyEachGameRatioResponse;
import com.game.rmt.domain.statistics.dto.response.MonthlyEachGameResponse;
import com.game.rmt.domain.statistics.dto.request.MonthlyGameRequest;
import com.game.rmt.domain.statistics.dto.request.MonthlyPlatformRequest;
import com.game.rmt.domain.statistics.dto.MonthlyStaticsDTO;
import com.game.rmt.domain.statistics.dto.response.MonthlyEachPlatformRatioResponse;
import com.game.rmt.domain.statistics.repository.custom.StaticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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

    public GameRatioEachPlatformResponse getGameRatioEachPlatform(GameRatioEachPlatformRequest request) {
        List<Platform> platformList = validateGameRatioEachPlatform(request);
        List<GamePriceDTO> gamePriceDTOList = getPriceEachGameByCondition(request);

        if (gamePriceDTOList == null || gamePriceDTOList.isEmpty()) {
            return new GameRatioEachPlatformResponse(
                    getPlatformNames(platformList),
                    new ArrayList<>());
        }

        return new GameRatioEachPlatformResponse(
                getPlatformNames(platformList),
                convertRatioList(gamePriceDTOList, sumTotalPrice(gamePriceDTOList))
        );
    }

    public MonthlyEachPlatformRatioResponse getMonthlyEachPlatformRatioByPreviousMonth(MonthlyPlatformRequest request) {
        Platform findPlatform = validateMonthlyPlatformStatics(request);
        List<MonthlyStaticsDTO> monthlyStatics = getMonthlyPlatformStaticsByCondition(request);

        return new MonthlyEachPlatformRatioResponse(findPlatform.getName(), getPreviousMonthRatioListByPlatform(monthlyStatics));
    }

    public MonthlyEachGameRatioResponse getMonthlyEachGameRatioByPreviousMonth(MonthlyGameRequest request) {
        Game findGame = validateMonthlyGameStatics(request);
        List<MonthlyStaticsDTO> monthlyStatics = getMonthlyGameStaticsByCondition(request);

        return new MonthlyEachGameRatioResponse(findGame.getName(), getPreviousMonthRatioListByGame(monthlyStatics));
    }

    private List<MonthlyRatioDTO> getPreviousMonthRatioListByPlatform(List<MonthlyStaticsDTO> monthlyStatics) {
        List<MonthlyRatioDTO> ratioList = new ArrayList<>();

        if (monthlyStatics == null || monthlyStatics.isEmpty()) {
            return ratioList;
        }

        for (int i = 0; i < monthlyStatics.size(); i++) {
            if (i == 0) {
                continue;
            }

            double currentPrice = monthlyStatics.get(i).getStaticValue();
            double previousPrice = monthlyStatics.get(i - 1).getStaticValue();

            ratioList.add(new MonthlyRatioDTO(monthlyStatics.get(i).getMonth(), currentPrice, previousPrice));
        }

        return ratioList;
    }

    private List<MonthlyRatioDTO> getPreviousMonthRatioListByGame(List<MonthlyStaticsDTO> monthlyStatics) {
        List<MonthlyRatioDTO> ratioList = new ArrayList<>();

        if (monthlyStatics == null || monthlyStatics.isEmpty()) {
            return ratioList;
        }

        for (int i = 0; i < monthlyStatics.size(); i++) {
            if (i == 0) {
                continue;
            }

            double currentPrice = monthlyStatics.get(i).getStaticValue();
            double previousPrice = monthlyStatics.get(i - 1).getStaticValue();

            ratioList.add(new MonthlyRatioDTO(monthlyStatics.get(i).getMonth(), currentPrice, previousPrice));
        }

        return ratioList;
    }

    private List<Platform> validateGameRatioEachPlatform(GameRatioEachPlatformRequest request) {
        request.isValidParam();
        return platformService.getPlatforms(request.getPlatformIds());
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

    private List<GamePriceDTO> getPriceEachGameByPlatformsAndStartDate(GameRatioEachPlatformRequest request) {
        return staticsRepository.findPriceEachGameByPlatformsAndStartDate(
                request.getPlatformIds(),
                request.getStartDate()
        );
    }

    private List<GamePriceDTO> getPriceEachGameByPlatformsAndEndDate(GameRatioEachPlatformRequest request) {
        return staticsRepository.findPriceEachGameByPlatformsAndEndDate(
                request.getPlatformIds(),
                request.getEndDate()
        );
    }

    private List<GamePriceDTO> getPriceEachGameByPlatformsBetweenDate(GameRatioEachPlatformRequest request) {
        return staticsRepository.findPriceEachGameByPlatformsBetweenDate(
                request.getPlatformIds(),
                request.getStartDate(),
                request.getEndDate()
        );
    }

    private List<GamePriceDTO> getPriceEachGameByCondition(GameRatioEachPlatformRequest request) {
        return switch (request.getRangeDateCondition()) {
            case RANGE_DATE -> getPriceEachGameByPlatformsBetweenDate(request);
            case ONLY_START_DATE -> getPriceEachGameByPlatformsAndStartDate(request);
            case ONLY_END_DATE -> getPriceEachGameByPlatformsAndEndDate(request);
            default -> staticsRepository.findPriceEachGameByPlatforms(request.getPlatformIds());
        };
    }

    private double sumTotalPrice(List<GamePriceDTO> priceDTOList) {
        return priceDTOList.stream()
                .mapToDouble(GamePriceDTO::getPrice)
                .sum();
    }

    private List<GameRatioDTO> convertRatioList(List<GamePriceDTO> priceDTOList, double totalPrice) {
        List<GameRatioDTO> gameRatioDTOList = new ArrayList<>();

        for (GamePriceDTO priceDTO : priceDTOList) {
            gameRatioDTOList.add(new GameRatioDTO(priceDTO.getGameName(), totalPrice, priceDTO.getPrice()));
        }

        return gameRatioDTOList;
    }

    private List<String> getPlatformNames(List<Platform> platformList) {
        return platformList.stream()
                .map(Platform::getName)
                .collect(Collectors.toList());
    }
}
