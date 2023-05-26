package com.game.rmt.domain.account.repository.custom;

import com.game.rmt.domain.account.domain.Account;
import com.game.rmt.domain.account.dto.AccountSearchFilter;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static com.game.rmt.domain.account.domain.QAccount.account;
import static com.game.rmt.domain.game.domain.QGame.game;
import static com.game.rmt.domain.platform.domain.QPlatform.platform;
import static com.game.rmt.domain.product.domain.QProduct.product;

public class AccountRepositoryImpl implements AccountRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public AccountRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Account> findAccountsByFilter(AccountSearchFilter filter, Pageable pageable) {
        return queryFactory
                .selectFrom(account)
                .join(account.product, product)
                .fetchJoin()
                .join(product.game, game)
                .fetchJoin()
                .join(game.platform, platform)
                .fetchJoin()
                .where(
                        searchPurchaseDateBySearchFilter(filter),
                        searchPriceBySearchFilter(filter),
                        searchProductNameBySearchFilter(filter),
                        searchGameIdsBySearchFilter(filter),
                        searchPlatformIdsBySearchFilter(filter)
                )
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }

    @Override
    public JPAQuery<Account> findAccountsByFilterQuery(AccountSearchFilter filter) {
        return queryFactory
                .selectFrom(account)
                .join(account.product, product)
                .fetchJoin()
                .join(product.game, game)
                .fetchJoin()
                .join(game.platform, platform)
                .fetchJoin()
                .where(
                        searchPurchaseDateBySearchFilter(filter),
                        searchPriceBySearchFilter(filter),
                        searchProductNameBySearchFilter(filter),
                        searchGameIdsBySearchFilter(filter),
                        searchPlatformIdsBySearchFilter(filter)
                );
    }

    @Override
    public Account findAccountById(Long accountId) {
        return queryFactory
                .selectFrom(account)
                .where(equalAccountId(accountId))
                .fetchFirst();
    }

    private BooleanExpression searchPlatformIdsBySearchFilter(AccountSearchFilter filter) {
        return filter.isValidSearchPlatformIdList() ? inPlatformIds(filter.getSearchPlatformIdList()) : null;
    }

    private BooleanExpression searchPriceBySearchFilter(AccountSearchFilter filter) {
        return filter.isValidSearchPrice() ? betweenPrice(filter.getSearchMinPrice(), filter.getSearchMaxPrice()) : null;
    }

    private BooleanExpression searchPurchaseDateBySearchFilter(AccountSearchFilter filter) {
        return filter.isValidSearchDate() ? betweenPurchaseDate(filter.getSearchStartDate(), filter.getSearchEndDate()) : null;
    }

    private BooleanExpression searchGameIdsBySearchFilter(AccountSearchFilter filter) {
        return filter.isValidGameIdList() ? inGameIds(filter.getSearchGameIdList()) : null;
    }

    private BooleanExpression searchProductNameBySearchFilter(AccountSearchFilter filter) {
        return filter.isValidProductName() ? containProductName(filter.getSearchProductName()) : null;
    }

    private BooleanExpression betweenPrice(int minPrice, int maxPrice) {
        return account.price.between(minPrice, maxPrice);
    }

    private BooleanExpression betweenPurchaseDate(LocalDate startDate, LocalDate endDate) {
        return account.purchaseDate.between(startDate, endDate);
    }

    private BooleanExpression containProductName(String productName) {
        return product.productName.contains(productName);
    }

    private BooleanExpression inGameIds(List<Long> gameIds) {
        return game.id.in(gameIds);
    }

    private BooleanExpression inPlatformIds(List<Long> platformIds) {
        return platform.id.in(platformIds);
    }
    private BooleanExpression equalAccountId(Long productId) {
        return account.id.eq(productId);
    }


}
