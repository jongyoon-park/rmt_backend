package com.game.rmt.domain.game.service;

import com.game.rmt.domain.account.domain.Account;
import com.game.rmt.domain.game.domain.Game;
import com.game.rmt.domain.game.dto.GameDTO;
import com.game.rmt.domain.game.dto.GameSearchFilter;
import com.game.rmt.domain.game.repository.GameRepository;
import com.game.rmt.domain.platform.domain.Platform;
import com.game.rmt.domain.product.domain.Product;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.game.rmt.domain.game.domain.QGame.game;
import static com.game.rmt.domain.platform.domain.QPlatform.platform;
import static com.game.rmt.domain.product.domain.QProduct.product;

@SpringBootTest
@Transactional
@Commit
class GameServiceTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @Autowired
    GameRepository gameRepository;

    private void initializePlatformTable() {
        Platform phone = new Platform("Phone");
        Platform pc = new Platform("PC");

        em.persist(phone);
        em.persist(pc);
    }

    private void initializeGameTable() {
        Platform pc = queryFactory
                .selectFrom(platform)
                .where(platform.name.eq("PC"))
                .fetchOne();

        Platform phone = queryFactory
                .selectFrom(platform)
                .where(platform.name.eq("Phone"))
                .fetchOne();

        Game lol = new Game("LOL", pc);
        Game umamusume = new Game("Umamusume", phone);

        em.persist(lol);
        em.persist(umamusume);
    }

    private void initializeProductTable() {
        Game lol = queryFactory
                .selectFrom(game)
                .where(game.name.eq("LOL"))
                .fetchOne();

        Game umamusume = queryFactory
                .selectFrom(game)
                .where(game.name.eq("Umamusume"))
                .fetchOne();

        Product fiveThousandJewel = new Product("5000 Jewel", umamusume);
        Product riotPoint = new Product("Riot Point", lol);

        em.persist(fiveThousandJewel);
        em.persist(riotPoint);
    }

    private void initializeAccountTable() {
        Product fiveThousandJewel = queryFactory
                .selectFrom(product)
                .where(product.productName.eq("5000 Jewel"))
                .fetchOne();

        Product riotPoint = queryFactory
                .selectFrom(product)
                .where(product.productName.eq("Riot Point"))
                .fetchOne();

        Account account1 = new Account(99000, LocalDate.now(), fiveThousandJewel);
        Account account2 = new Account(15000, LocalDate.now(), "note", riotPoint);
    }

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
        initializePlatformTable();
        initializeGameTable();
        initializeProductTable();
        initializeAccountTable();

        em.flush();
        em.clear();
    }

    @Test
    public void getGames() {
        em.flush();
        em.clear();

        GameSearchFilter filter = new GameSearchFilter("uma");

        //eq(null) is not allowed. Use isNull() instead
        //null에 대한 예외 처리 필요

        /*List<Game> games = queryFactory
                .select(game)
                .from(game)
                .join(game.platform, platform)
                .fetchJoin()
                .where(game.name.contains(filter.getSearchGameName()))
                .fetch();*/

        List<Game> games = gameRepository.findGames(filter);


        List<GameDTO> gameDTOList = new ArrayList<>();

        for (Game game : games) {
            System.out.println("!!!!!! platform Name : " + game.getPlatform().getName());
            gameDTOList.add(new GameDTO(game));
        }

        Assertions.assertThat(gameDTOList.size()).isEqualTo(1);
    }
}