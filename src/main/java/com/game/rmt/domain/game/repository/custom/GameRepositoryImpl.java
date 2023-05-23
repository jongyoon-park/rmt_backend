package com.game.rmt.domain.game.repository.custom;

import com.game.rmt.domain.game.domain.Game;
import com.game.rmt.domain.game.dto.GameSearchFilter;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.game.rmt.domain.game.domain.QGame.game;
import static com.game.rmt.domain.platform.domain.QPlatform.platform;

public class GameRepositoryImpl implements GameRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public GameRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Game> findGames(GameSearchFilter gameSearchFilter) {
        return queryFactory
                .select(game)
                .from(game)
                .join(game.platform, platform)
                .fetchJoin()
                .where(
                        searchGameNameBySearchFilter(gameSearchFilter),
                        searchPlatformIdBySearchFilter(gameSearchFilter)
                )
                .fetch();
    }

    @Override
    public Game findGameById(Long gameId) {
        return queryFactory
                .selectFrom(game)
                .where(equalGameId(gameId))
                .fetchFirst();
    }

    private BooleanExpression searchGameNameBySearchFilter(GameSearchFilter gameSearchFilter) {
        return gameSearchFilter.isValidSearchGameName() ? containGameName(gameSearchFilter.getSearchGameName()) : null;
    }

    private BooleanExpression searchPlatformIdBySearchFilter(GameSearchFilter gameSearchFilter) {
        return gameSearchFilter.isValidSearchPlatformId() ? equalPlatformId(gameSearchFilter.getSearchPlatformId()) : null;
    }

    private BooleanExpression equalPlatformId(long platformId) {
        return game.platform.id.eq(platformId);
    }

    private BooleanExpression containGameName(String gameName) {
        return game.name.contains(gameName);
    }

    private BooleanExpression equalGameId(long gameId) {
        return game.id.eq(gameId);
    }
}
