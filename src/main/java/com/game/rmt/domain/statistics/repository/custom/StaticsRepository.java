package com.game.rmt.domain.statistics.repository.custom;

import com.game.rmt.domain.statistics.dto.GamePriceDTO;
import com.game.rmt.domain.statistics.dto.MonthlyStaticsDTO;
import com.game.rmt.domain.statistics.dto.QGamePriceDTO;
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
import static com.game.rmt.domain.platform.domain.QPlatform.platform;
import static com.game.rmt.domain.product.domain.QProduct.product;

@Repository
public class StaticsRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private final int ONE_YEAR = 1;
    private final int THREE_YEARS = 3;

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
                        minusYearByPurchaseDateDefaultRange(LocalDate.now()),
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
                        minusYearByPurchaseDateFromEndDate(endDate),
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
    public List<MonthlyStaticsDTO> findMonthlyStaticsByPlatformId(long platformId) {
        StringTemplate monthFormat = convertMonthTemplateByPurchaseDate();

        return queryFactory
                .select(new QMonthlyStaticsDTO(monthFormat, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .join(game.platform, platform)
                .where(
                        minusYearByPurchaseDateFromEndDate(LocalDate.now()),
                        eqPlatformId(platformId)
                )
                .groupBy(monthFormat)
                .orderBy(monthFormat.asc())
                .fetch();
    }

    public List<MonthlyStaticsDTO> findMonthlyStaticsByPlatformIdAndStartDate(long platformId, LocalDate startDate) {
        StringTemplate monthFormat = convertMonthTemplateByPurchaseDate();

        return queryFactory
                .select(new QMonthlyStaticsDTO(monthFormat, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .join(game.platform, platform)
                .where(
                        plusYearByPurchaseDateFromStartDate(startDate),
                        eqPlatformId(platformId)
                )
                .groupBy(monthFormat)
                .orderBy(monthFormat.asc())
                .fetch();
    }

    public List<MonthlyStaticsDTO> findMonthlyStaticsByPlatformIdAndEndDate(long platformId, LocalDate endDate) {
        StringTemplate monthFormat = convertMonthTemplateByPurchaseDate();

        return queryFactory
                .select(new QMonthlyStaticsDTO(monthFormat, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .join(game.platform, platform)
                .where(
                        minusYearByPurchaseDateFromEndDate(endDate),
                        eqPlatformId(platformId)
                )
                .groupBy(monthFormat)
                .orderBy(monthFormat.asc())
                .fetch();
    }

    public List<MonthlyStaticsDTO> findMonthlyStaticsByPlatformIdBetweenDate(long platformId, LocalDate startDate, LocalDate endDate) {
        StringTemplate monthFormat = convertMonthTemplateByPurchaseDate();

        return queryFactory
                .select(new QMonthlyStaticsDTO(monthFormat, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .join(game.platform, platform)
                .where(
                        betweenPurchaseDate(startDate, endDate),
                        eqPlatformId(platformId)
                )
                .groupBy(monthFormat)
                .orderBy(monthFormat.asc())
                .fetch();
    }

    public List<GamePriceDTO> findPriceEachGameByPlatforms(List<Long> platformIds) {
        return queryFactory
                .select(new QGamePriceDTO(game.name, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .join(game.platform, platform)
                .where(
                        minusYearByPurchaseDateDefaultRange(LocalDate.now()),
                        eqPlatformIds(platformIds)
                )
                .groupBy(game.id)
                //orderBy를 이용한 최적화 필요
                .fetch();
    }

    public List<GamePriceDTO> findPriceEachGameByPlatformsAndStartDate(List<Long> platformIds, LocalDate startDate) {
        return queryFactory
                .select(new QGamePriceDTO(game.name, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .join(game.platform, platform)
                .where(
                        plusYearByPurchaseDateFromStartDate(startDate),
                        eqPlatformIds(platformIds)
                )
                .groupBy(game.id)
                //orderBy를 이용한 최적화 필요
                .fetch();
    }

    public List<GamePriceDTO> findPriceEachGameByPlatformsAndEndDate(List<Long> platformIds, LocalDate endDate) {
        return queryFactory
                .select(new QGamePriceDTO(game.name, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .join(game.platform, platform)
                .where(
                        minusYearByPurchaseDateFromEndDate(endDate),
                        eqPlatformIds(platformIds)
                )
                .groupBy(game.id)
                //orderBy를 이용한 최적화 필요
                .fetch();
    }

    public List<GamePriceDTO> findPriceEachGameByPlatformsBetweenDate(List<Long> platformIds, LocalDate startDate, LocalDate endDate) {
        return queryFactory
                .select(new QGamePriceDTO(game.name, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .join(game.platform, platform)
                .where(
                        betweenPurchaseDate(startDate, endDate),
                        eqPlatformIds(platformIds)
                )
                .groupBy(game.id)
                //orderBy를 이용한 최적화 필요
                .fetch();
    }

    private BooleanExpression minusYearByPurchaseDateDefaultRange(LocalDate currentDate) {
        return betweenPurchaseDate(currentDate.minusYears(THREE_YEARS), currentDate);
    }

    private BooleanExpression minusYearByPurchaseDateFromEndDate(LocalDate endDate) {
        return betweenPurchaseDate(endDate.minusYears(ONE_YEAR), endDate);
    }

    private BooleanExpression plusYearByPurchaseDateFromStartDate(LocalDate startDate) {
        return betweenPurchaseDate(startDate, startDate.plusYears(ONE_YEAR));
    }

    private BooleanExpression betweenPurchaseDate(LocalDate startDate, LocalDate endDate) {
        return account.purchaseDate.between(startDate, endDate);
    }

    private BooleanExpression eqGameId(long gameId) {
        return account.product.game.id.eq(gameId);
    }

    private BooleanExpression eqPlatformId(long platformId) {
        return platform.id.eq(platformId);
    }

    private BooleanExpression eqPlatformIds(List<Long> platformIds) {
        return platform.id.in(platformIds);
    }

    private StringTemplate convertMonthTemplateByPurchaseDate() {
        return Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                account.purchaseDate,
                ConstantImpl.create("%Y-%m"));
    }
}
