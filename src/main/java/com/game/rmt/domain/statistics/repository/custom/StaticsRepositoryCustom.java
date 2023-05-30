package com.game.rmt.domain.statistics.repository.custom;

import com.game.rmt.domain.statistics.dto.MonthlyStaticsDTO;

import java.util.List;

public interface StaticsRepositoryCustom {
    List<MonthlyStaticsDTO> getMonthlyStaticsByGameId(Long gameId);
    List<MonthlyStaticsDTO> getMonthlyStaticsByGameIdAndStartDate(Long gameId, String startDate);
    List<MonthlyStaticsDTO> getMonthlyStaticsByGameIdAndEndDate(Long gameId, String endDate);
    List<MonthlyStaticsDTO> getMonthlyStaticsByGameIdBetweenDate(Long gameId, String startDate, String endDate);
}
