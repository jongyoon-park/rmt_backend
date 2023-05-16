package com.game.rmt;

import com.game.rmt.game.Game;
import com.game.rmt.platform.Platform;
import com.game.rmt.product.Product;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

import static com.game.rmt.game.QGame.game;
import static com.game.rmt.platform.QPlatform.platform;

@SpringBootTest
@Transactional
@Commit
class RmtApplicationTests {

	@Autowired
	EntityManager em;

	JPAQueryFactory queryFactory;

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

	@BeforeEach
	public void before() {
		queryFactory = new JPAQueryFactory(em);
		initializePlatformTable();
		initializeGameTable();
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void insertPlatform() {
		Platform phone = new Platform("Phone");
		Platform pc = new Platform("PC");

		em.persist(phone);
		em.persist(pc);

		em.flush();
		em.clear();

		List<Platform> findPlatform = queryFactory
				.select(platform)
				.from(platform)
				.fetch();

		Assertions.assertThat(findPlatform.size()).isEqualTo(2);
	}

	@Test
	public void insertGame() {
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

		em.flush();
		em.clear();

		List<Game> findGames = queryFactory
				.selectFrom(game)
				.fetch();

		Assertions.assertThat(findGames.size()).isEqualTo(2);
	}

	@Test
	public void insertProduct() {
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



}
