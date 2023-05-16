package com.game.rmt.domain.platform.service;

import com.game.rmt.domain.account.domain.Account;
import com.game.rmt.domain.game.domain.Game;
import com.game.rmt.domain.platform.domain.Platform;
import com.game.rmt.domain.platform.dto.PlatformDTO;
import com.game.rmt.domain.platform.repository.PlatformRepository;
import com.game.rmt.domain.product.domain.Product;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@Commit
class PlatformServiceTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @Autowired
    PlatformRepository platformRepository;

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
    public void getPlatforms() {
        em.flush();
        em.clear();

        List<Platform> platforms = platformRepository.findAll();

        List<PlatformDTO> platformDTOList = new ArrayList<>();

        for (Platform platform : platforms) {
            platformDTOList.add(new PlatformDTO(platform));
        }

        assertEquals(platformDTOList.size(), 2);
    }
}