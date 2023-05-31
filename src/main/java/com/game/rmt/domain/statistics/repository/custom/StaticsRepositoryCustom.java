package com.game.rmt.domain.statistics.repository.custom;

import com.game.rmt.domain.statistics.dto.MonthlyStaticsDTO;

import java.time.LocalDate;
import java.util.List;

public interface StaticsRepositoryCustom {
    List<MonthlyStaticsDTO> getMonthlyStaticsByGameId(long gameId);
    List<MonthlyStaticsDTO> getMonthlyStaticsByGameIdAndStartDate(long gameId, LocalDate startDate);
    List<MonthlyStaticsDTO> getMonthlyStaticsByGameIdAndEndDate(long gameId, LocalDate endDate);
    List<MonthlyStaticsDTO> getMonthlyStaticsByGameIdBetweenDate(long gameId, LocalDate startDate, LocalDate endDate);
}
