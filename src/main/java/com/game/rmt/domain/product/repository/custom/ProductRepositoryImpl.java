package com.game.rmt.domain.product.repository.custom;

import com.game.rmt.domain.platform.domain.QPlatform;
import com.game.rmt.domain.product.domain.Product;
import com.game.rmt.domain.product.domain.QProduct;
import com.game.rmt.domain.product.dto.ProductSearchFilter;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.game.rmt.domain.game.domain.QGame.game;
import static com.game.rmt.domain.platform.domain.QPlatform.platform;
import static com.game.rmt.domain.product.domain.QProduct.product;

public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Product> findProducts(ProductSearchFilter productSearchFilter) {
        return queryFactory
                .selectFrom(product)
                .join(product.game, game)
                .fetchJoin()
                .join(game.platform, platform)
                .fetchJoin()
                .where(
                        searchPlatformIdBySearchFilter(productSearchFilter),
                        searchGameIdBySearchFilter(productSearchFilter),
                        searchIsActivatedBySearchFilter(productSearchFilter)
                )
                .fetch();
    }

    private BooleanExpression searchPlatformIdBySearchFilter(ProductSearchFilter productSearchFilter) {
        return productSearchFilter.isValidSearchPlatformId() ?
                equalPlatformId(productSearchFilter.getSearchPlatformId()) : null;
    }

    private BooleanExpression searchGameIdBySearchFilter(ProductSearchFilter productSearchFilter) {
        return productSearchFilter.isValidSearchGameId() ?
                equalGameId(productSearchFilter.getSearchGameId()) : null;
    }

    private BooleanExpression searchIsActivatedBySearchFilter(ProductSearchFilter productSearchFilter) {
        return productSearchFilter.isValidSearchIsActivated() ?
                equalIsActivated(productSearchFilter.getSearchIsActivated()) : equalIsActivated(true);
    }

    private BooleanExpression equalPlatformId(long platformId) {
        return platform.id.eq(platformId);
    }

    private BooleanExpression equalGameId(long gameId) {
        return game.id.eq(gameId);
    }

    private BooleanExpression equalIsActivated(boolean isActivated) {
        return product.isActivated.eq(isActivated);
    }

}
