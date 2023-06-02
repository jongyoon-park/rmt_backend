package com.game.rmt.domain.platform.repository.custom;

import com.game.rmt.domain.platform.domain.Platform;
import com.game.rmt.domain.platform.domain.QPlatform;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import java.util.List;

import static com.game.rmt.domain.platform.domain.QPlatform.platform;

public class PlatformRepositoryImpl implements PlatformRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PlatformRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Platform findPlatformById(Long id) {
        return queryFactory
                .select(platform)
                .from(platform)
                .where(eqPlatformId(id))
                .fetchFirst();
    }

    @Override
    public List<Platform> findPlatformByIds(List<Long> ids) {
        return queryFactory
                .select(platform)
                .from(platform)
                .where(eqPlatformIds(ids))
                .fetch();
    }

    private BooleanExpression eqPlatformId(long platformId) {
        return platform.id.eq(platformId);
    }

    private BooleanExpression eqPlatformIds(List<Long> platformIds) {
        return platform.id.in(platformIds);
    }
}
