package com.game.rmt.domain.statistics.controller;

import com.game.rmt.domain.statistics.dto.response.MonthlyEachGameResponse;
import com.game.rmt.domain.statistics.dto.request.MonthlyGameRequest;
import com.game.rmt.domain.statistics.dto.request.MonthlyPlatformRequest;
import com.game.rmt.domain.statistics.service.StatisticsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("rmt/statics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/monthly/game")
    public MonthlyEachGameResponse getMonthlyGameStatics(@RequestBody MonthlyGameRequest request) {
        return statisticsService.getMonthlyGameStatics(request);
    }

    @GetMapping("/monthly/platform")
    public MonthlyEachGameResponse getMonthlyPlatformStatics(@RequestBody MonthlyPlatformRequest request) {
        return statisticsService.getMonthlyPlatformStatics(request);
    }
}
