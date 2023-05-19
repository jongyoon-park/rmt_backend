package com.game.rmt.domain.platform.repository.custom;

import com.game.rmt.domain.platform.domain.Platform;
import com.game.rmt.domain.platform.domain.QPlatform;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

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
                .where(platform.id.eq(id))
                .fetchOne();
    }
}
