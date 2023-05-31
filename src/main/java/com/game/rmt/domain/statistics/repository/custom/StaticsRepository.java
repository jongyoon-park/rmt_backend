package com.game.rmt.domain.statistics.repository.custom;

import com.game.rmt.domain.statistics.dto.MonthlyStaticsDTO;
import com.game.rmt.domain.statistics.dto.QMonthlyStaticsDTO;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static com.game.rmt.domain.account.domain.QAccount.account;
import static com.game.rmt.domain.game.domain.QGame.game;
import static com.game.rmt.domain.product.domain.QProduct.product;

@Repository
public class StaticsRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public StaticsRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<MonthlyStaticsDTO> findMonthlyStaticsByGameId(long gameId) {
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

    public List<MonthlyStaticsDTO> findMonthlyStaticsByGameIdAndStartDate(long gameId, LocalDate startDate) {
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

    public List<MonthlyStaticsDTO> findMonthlyStaticsByGameIdAndEndDate(long gameId, LocalDate endDate) {
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

    public List<MonthlyStaticsDTO> findMonthlyStaticsByGameIdBetweenDate(long gameId, LocalDate startDate, LocalDate endDate) {
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
