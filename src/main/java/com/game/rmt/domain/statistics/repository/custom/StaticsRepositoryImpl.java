package com.game.rmt.domain.statistics.repository.custom;

import com.game.rmt.domain.statistics.dto.MonthlyStaticsDTO;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;

import java.util.List;

import static com.game.rmt.domain.account.domain.QAccount.account;

public class StaticsRepositoryImpl implements StaticsRepositoryCustom {
    @Override
    public List<MonthlyStaticsDTO> getMonthlyStaticsByGameId(Long gameId) {
        return null;
    }

    @Override
    public List<MonthlyStaticsDTO> getMonthlyStaticsByGameIdAndStartDate(Long gameId, String startDate) {
        return null;
    }

    @Override
    public List<MonthlyStaticsDTO> getMonthlyStaticsByGameIdAndEndDate(Long gameId, String endDate) {
        return null;
    }

    @Override
    public List<MonthlyStaticsDTO> getMonthlyStaticsByGameIdBetweenDate(Long gameId, String startDate, String endDate) {
        return null;
    }

    private StringTemplate convertMonthTemplateByPurchaseDate() {
        return Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                account.purchaseDate,
                ConstantImpl.create("%Y-%m"));
    }
}
