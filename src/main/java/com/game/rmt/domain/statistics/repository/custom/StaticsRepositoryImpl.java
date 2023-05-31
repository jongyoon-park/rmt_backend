package com.game.rmt.domain.statistics.repository.custom;

import com.game.rmt.domain.statistics.dto.MonthlyStaticsDTO;
import com.game.rmt.domain.statistics.dto.QMonthlyStaticsDTO;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static com.game.rmt.domain.account.domain.QAccount.account;
import static com.game.rmt.domain.game.domain.QGame.game;
import static com.game.rmt.domain.product.domain.QProduct.product;

public class StaticsRepositoryImpl implements StaticsRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public StaticsRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MonthlyStaticsDTO> getMonthlyStaticsByGameId(long gameId) {
        StringTemplate monthFormat = convertMonthTemplateByPurchaseDate();

        return queryFactory
                .select(new QMonthlyStaticsDTO(monthFormat, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .where(
                        minusYearByByPurchaseDateFromEndDate(LocalDate.now()),
                        eqGameId(gameId)
                )
                .groupBy(monthFormat)
                .orderBy(monthFormat.asc())
                .fetch();
    }

    @Override
    public List<MonthlyStaticsDTO> getMonthlyStaticsByGameIdAndStartDate(long gameId, LocalDate startDate) {
        StringTemplate monthFormat = convertMonthTemplateByPurchaseDate();

        return queryFactory
                .select(new QMonthlyStaticsDTO(monthFormat, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .where(
                        plusYearByPurchaseDateFromStartDate(startDate),
                        eqGameId(gameId)
                )
                .groupBy(monthFormat)
                .orderBy(monthFormat.asc())
                .fetch();
    }

    @Override
    public List<MonthlyStaticsDTO> getMonthlyStaticsByGameIdAndEndDate(long gameId, LocalDate endDate) {
        StringTemplate monthFormat = convertMonthTemplateByPurchaseDate();

        return queryFactory
                .select(new QMonthlyStaticsDTO(monthFormat, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .where(
                        minusYearByByPurchaseDateFromEndDate(endDate),
                        eqGameId(gameId)
                )
                .groupBy(monthFormat)
                .orderBy(monthFormat.asc())
                .fetch();
    }

    @Override
    public List<MonthlyStaticsDTO> getMonthlyStaticsByGameIdBetweenDate(long gameId, LocalDate startDate, LocalDate endDate) {
        StringTemplate monthFormat = convertMonthTemplateByPurchaseDate();

        return queryFactory
                .select(new QMonthlyStaticsDTO(monthFormat, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .where(
                        betweenPurchaseDate(startDate, endDate),
                        eqGameId(gameId)
                )
                .groupBy(monthFormat)
                .orderBy(monthFormat.asc())
                .fetch();
    }

    private BooleanExpression minusYearByByPurchaseDateFromEndDate(LocalDate endDate) {
        return betweenPurchaseDate(endDate.minusYears(1), endDate);
    }

    private BooleanExpression plusYearByPurchaseDateFromStartDate(LocalDate startDate) {
        return betweenPurchaseDate(startDate, startDate.plusYears(1));
    }

    private BooleanExpression betweenPurchaseDate(LocalDate startDate, LocalDate endDate) {
        return account.purchaseDate.between(startDate, endDate);
    }

    private BooleanExpression eqGameId(long gameId) {
        return account.product.game.id.eq(gameId);
    }

    private StringTemplate convertMonthTemplateByPurchaseDate() {
        return Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                account.purchaseDate,
                ConstantImpl.create("%Y-%m"));
    }
}
