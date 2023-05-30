package com.game.rmt.domain.statistics.service;

import com.game.rmt.domain.account.domain.Account;
import com.game.rmt.domain.account.repository.AccountRepository;
import com.game.rmt.domain.game.domain.Game;
import com.game.rmt.domain.game.repository.GameRepository;
import com.game.rmt.domain.platform.domain.Platform;
import com.game.rmt.domain.platform.repository.PlatformRepository;
import com.game.rmt.domain.platform.service.PlatformService;
import com.game.rmt.domain.product.domain.Product;
import com.game.rmt.domain.product.repository.ProductRepository;
import com.game.rmt.domain.statistics.dto.MonthlyEachGameResponse;
import com.game.rmt.domain.statistics.dto.MonthlyGameRequest;
import com.game.rmt.domain.statistics.dto.MonthlyStaticsDTO;
import com.game.rmt.domain.statistics.dto.QMonthlyStaticsDTO;
import com.game.rmt.global.errorhandler.exception.ErrorCode;
import com.game.rmt.global.errorhandler.exception.NotFoundException;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static com.game.rmt.domain.account.domain.QAccount.account;
import static com.game.rmt.domain.game.domain.QGame.game;
import static com.game.rmt.domain.platform.domain.QPlatform.platform;
import static com.game.rmt.domain.product.domain.QProduct.product;

@SpringBootTest
@Transactional
@Commit
class StatisticsServiceTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    PlatformRepository platformRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PlatformService platformService;

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

        em.persist(account1);
        em.persist(account2);
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
    public void getMonthlyEachGameStatics() {
        // 통계 기반 정보 받아오기 : gameId, startDate, endDate
        MonthlyGameRequest request = new MonthlyGameRequest((long) 3, LocalDate.parse("2022-06-01"), LocalDate.parse("2022-06-30"));
        // 통계 기반 정보 유효성 체크
        request.isValidParam();
        Game findGame = queryFactory
                .selectFrom(game)
                .where(game.id.eq(request.getGameId()))
                .fetchFirst();

        if (findGame == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_GAME);
        }

        /*
        * 통계 날짜 기준
        * 1. startDate, endDate 둘 다 null : 현재 날짜 기준으로 1년 이전의 데이터
        * 2. startDate만 값이 있을 경우 startDate 기준으로 1년 이후의 데이터까지 통계
        * 3. endDate만 값이 있을 경우 endDate 기준으로 1년 이전의 데이터까지 통계
        * */
        // 통계 데이터 가져오기
        // 통계 데이터 쿼리
        // 1. startDate, endDate 둘 다 null
        /*
        *SELECT DATE_FORMAT(purchase_date, '%Y-%m'), SUM(price)
        FROM account a
        WHERE DATE_FORMAT(purchase_date, '%Y-%m') >= DATE_FORMAT(DATE_SUB(NOW(), INTERVAL 1 YEAR), '%Y-%m')
        GROUP BY DATE_FORMAT(purchase_date, '%Y-%m');
        * */

        em.flush();
        em.clear();

        LocalDate now = LocalDate.now();
        StringTemplate formattedDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                account.purchaseDate,
                ConstantImpl.create("%Y-%m"));

        List<MonthlyStaticsDTO> fetch1 = queryFactory
                .select(new QMonthlyStaticsDTO(formattedDate, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .where(
                        account.purchaseDate.between(LocalDate.now().minusYears(1), LocalDate.now()),
                        account.product.game.id.eq(request.getGameId())
                )
                .groupBy(formattedDate)
                .orderBy(formattedDate.asc())
                .fetch();

        System.out.println("!!!!!! test");
        // 2. startDate만 값이 있을 경우
        /*SELECT DATE_FORMAT(purchase_date, '%Y-%m'), SUM(price)
        FROM account a
        WHERE purchase_date BETWEEN DATE_FORMAT('2023-05-30', '%Y-%m-%d') and DATE_ADD(DATE_FORMAT('2023-05-30', '%Y-%m-%d'), INTERVAL 1 YEAR)
        GROUP BY DATE_FORMAT(purchase_date, '%Y-%m');*/

        List<MonthlyStaticsDTO> fetch2 = queryFactory
                .select(new QMonthlyStaticsDTO(formattedDate, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .where(
                        account.purchaseDate.between(request.getStartDate(), request.getStartDate().plusYears(1)),
                        account.product.game.id.eq(request.getGameId())
                )
                .groupBy(formattedDate)
                .orderBy(formattedDate.asc())
                .fetch();


        // 3. endDate만 값이 있을 경우
        /*SELECT DATE_FORMAT(purchase_date, '%Y-%m'), SUM(price)
        FROM account a
        WHERE purchase_date BETWEEN DATE_SUB(DATE_FORMAT('2023-05-30', '%Y-%m-%d'), INTERVAL 1 YEAR) and DATE_FORMAT('2023-05-30', '%Y-%m-%d')
        GROUP BY DATE_FORMAT(purchase_date, '%Y-%m');*/
        List<MonthlyStaticsDTO> fetch3 = queryFactory
                .select(new QMonthlyStaticsDTO(formattedDate, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .where(
                        account.purchaseDate.between(request.getEndDate().minusYears(1), request.getEndDate()),
                        account.product.game.id.eq(request.getGameId())
                )
                .groupBy(formattedDate)
                .orderBy(formattedDate.asc())
                .fetch();

        // 4. 둘 다 있는 경우
        /*SELECT DATE_FORMAT(purchase_date, '%Y-%m'), SUM(price)
        FROM account a
        WHERE purchase_date BETWEEN DATE_SUB(DATE_FORMAT('2023-05-30', '%Y-%m-%d'), INTERVAL 1 YEAR) and DATE_FORMAT('2023-06-30', '%Y-%m-%d')
        GROUP BY DATE_FORMAT(purchase_date, '%Y-%m');*/
        List<MonthlyStaticsDTO> fetch4 = queryFactory
                .select(new QMonthlyStaticsDTO(formattedDate, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .where(
                        account.purchaseDate.between(request.getStartDate(), request.getEndDate()),
                        account.product.game.id.eq(request.getGameId())
                )
                .groupBy(formattedDate)
                .orderBy(formattedDate.asc())
                .fetch();

        // 통계 데이터를 MonthlyEachGameResponse로 변환
        MonthlyEachGameResponse response = new MonthlyEachGameResponse(findGame.getName(), fetch1);
    }
}