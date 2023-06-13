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
import com.game.rmt.domain.statistics.dto.*;
import com.game.rmt.domain.statistics.dto.request.GameRatioEachPlatformRequest;
import com.game.rmt.domain.statistics.dto.request.MonthlyGameRequest;
import com.game.rmt.domain.statistics.dto.request.MonthlyPlatformRequest;
import com.game.rmt.domain.statistics.dto.GameRatioDTO;
import com.game.rmt.domain.statistics.dto.response.MonthlyEachGameResponse;
import com.game.rmt.domain.statistics.dto.response.MonthlyEachPlatformResponse;
import com.game.rmt.global.errorhandler.exception.ErrorCode;
import com.game.rmt.global.errorhandler.exception.NotFoundException;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
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
import java.util.Arrays;
import java.util.List;

import static com.game.rmt.domain.account.domain.QAccount.account;
import static com.game.rmt.domain.game.domain.QGame.game;
import static com.game.rmt.domain.platform.domain.QPlatform.platform;
import static com.game.rmt.domain.product.domain.QProduct.product;
import static org.assertj.core.api.Assertions.assertThat;

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

        Account account1 = new Account(100000, LocalDate.now(), fiveThousandJewel);
        Account account3 = new Account(90000, LocalDate.parse("2023-05-01"), fiveThousandJewel);
        Account account4 = new Account(110000, LocalDate.parse("2023-04-01"), fiveThousandJewel);
        Account account5 = new Account(99000, LocalDate.parse("2023-03-01"), fiveThousandJewel);
        Account account7 = new Account(120000, LocalDate.parse("2023-02-01"), fiveThousandJewel);
        Account account6 = new Account(130000, LocalDate.parse("2023-01-01"), fiveThousandJewel);
        Account account2 = new Account(15000, LocalDate.parse("2023-06-01"), "note", riotPoint);

        em.persist(account1);
        em.persist(account2);
        em.persist(account3);
        em.persist(account4);
        em.persist(account5);
        em.persist(account6);
        em.persist(account7);
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

    @Test
    public void getMonthlyEachPlatformStatics() {
        // 통계 기반 정보 받아오기 : platformId, startDate, endDate
        MonthlyPlatformRequest request = new MonthlyPlatformRequest((long) 1, LocalDate.parse("2022-06-01"), LocalDate.parse("2022-06-30"));
        // 통계 기반 정보 유효성 체크
        request.isValidParam();
        Platform findPlatform = queryFactory
                .select(platform)
                .from(platform)
                .where(platform.id.eq(request.getPlatformId()))
                .fetchFirst();

        if (findPlatform == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_PLATFORM);
        }

        em.flush();
        em.clear();

        // 통계 데이터 가져오기
        // platform까지 같이 join 해야 함

        LocalDate now = LocalDate.now();
        StringTemplate formattedDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                account.purchaseDate,
                ConstantImpl.create("%Y-%m"));

        RangeDate rangeDateCondition = request.getRangeDateCondition();

        if (rangeDateCondition.equals(RangeDate.RANGE_DATE)) {
            List<MonthlyStaticsDTO> fetch4 = queryFactory
                    .select(new QMonthlyStaticsDTO(formattedDate, account.price.sum()))
                    .from(account)
                    .join(account.product, product)
                    .join(product.game, game)
                    .join(game.platform, platform)
                    .where(
                            account.purchaseDate.between(request.getStartDate(), request.getEndDate()),
                            platform.id.eq(request.getPlatformId())
                    )
                    .groupBy(formattedDate)
                    .orderBy(formattedDate.asc())
                    .fetch();
        }

        List<MonthlyStaticsDTO> fetch1 = queryFactory
                .select(new QMonthlyStaticsDTO(formattedDate, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .join(game.platform, platform)
                .where(
                        account.purchaseDate.between(now.minusYears(1), now),
                        platform.id.eq(request.getPlatformId())
                )
                .groupBy(formattedDate)
                .orderBy(formattedDate.asc())
                .fetch();

        List<MonthlyStaticsDTO> fetch2 = queryFactory
                .select(new QMonthlyStaticsDTO(formattedDate, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .join(game.platform, platform)
                .where(
                        account.purchaseDate.between(request.getStartDate(), request.getStartDate().plusYears(1)),
                        platform.id.eq(request.getPlatformId())
                )
                .groupBy(formattedDate)
                .orderBy(formattedDate.asc())
                .fetch();

        List<MonthlyStaticsDTO> fetch3 = queryFactory
                .select(new QMonthlyStaticsDTO(formattedDate, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .join(game.platform, platform)
                .where(
                        account.purchaseDate.between(request.getEndDate().minusYears(1), request.getEndDate()),
                        platform.id.eq(request.getPlatformId())
                )
                .groupBy(formattedDate)
                .orderBy(formattedDate.asc())
                .fetch();

        List<MonthlyStaticsDTO> fetch4 = queryFactory
                .select(new QMonthlyStaticsDTO(formattedDate, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .join(game.platform, platform)
                .where(
                        account.purchaseDate.between(request.getStartDate(), request.getEndDate()),
                        platform.id.eq(request.getPlatformId())
                )
                .groupBy(formattedDate)
                .orderBy(formattedDate.asc())
                .fetch();

        // 통계 데이터 변환 작업
        MonthlyEachPlatformResponse response = new MonthlyEachPlatformResponse(findPlatform.getName(), fetch1);
        assertThat(fetch1.size()).isEqualTo(1);
    }

    @Test
    public void getRatioEachGame() {
        // 통계 기반 정보 받아오기 : platformIds, startDate, endDate
        List<Long> platformIds = Arrays.asList((long) 1, (long) 2);
        GameRatioEachPlatformRequest request = new GameRatioEachPlatformRequest(platformIds, null, null);
        // 통계 기반 정보 유효성 체크
        request.isValidParam();
        // platformIds의 length가 0일 경우(또는 null) 반려
        // platformIds가 없으면서 전체 기간이 없을 경우
        /*select g.name, sum(account.price)
        from account
        right join product p on p.id = account.product_id
        right join game g on g.id = p.game_id
        group by game_id;
        */
        List<GamePriceDTO> fetch = queryFactory
                .select(new QGamePriceDTO(game.name, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .groupBy(game.name)
                .fetch();

        List<GamePriceDTO> fetch1 = queryFactory
                .select(new QGamePriceDTO(game.name, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .join(game.platform, platform)
                .where(platform.id.in(request.getPlatformIds()))
                .groupBy(game.name)
                .fetch();

        // startDate, endDate가 없을 경우 전체 기간으로 조회
        /*List<GameTotalPriceDTO> fetch2 = queryFactory
                .select(new QGameTotalPriceDTO(game.name, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .where(
                        account.purchaseDate.between(request.getStartDate(), request.getEndDate())
                )
                .groupBy(game.name)
                .fetch();*/
        // startDate, endDate 둘 중 하나만 있다면 해당 일 기준 1년 데이터 조회
        /*List<GameTotalPriceDTO> fetch3 = queryFactory
                .select(new QGameTotalPriceDTO(game.name, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .where(
                        account.purchaseDate.between(request.getStartDate(), request.getStartDate().plusYears(1))
                )
                .groupBy(game.name)
                .fetch();*/

        /*List<GameTotalPriceDTO> fetch3 = queryFactory
                .select(new QGameTotalPriceDTO(game.name, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .where(
                        account.purchaseDate.between(request.getEndDate.minusYears(1), request.getEndDate())
                )
                .groupBy(game.name)
                .fetch();*/
        // 데이터를 담을 DTO : RatioEachGameResponse
        double totalPrice = totalPrice(fetch);

        // 통계 비율 구하기
        List<GameRatioDTO> ratioEachGameDTOList = new ArrayList<>();

        List<Double> percentageList = new ArrayList<>();
        fetch.forEach(gamePriceDTO -> {
            ratioEachGameDTOList.add(new GameRatioDTO(gamePriceDTO.getGameName(), totalPrice, gamePriceDTO.getPrice()));
//            percentageList.add(Math.round((double) gameTotalPriceDTO.getPrice() / totalPrice * 100.0 * 100.0) / 100.0);
        });
//        Assertions.assertThat(percentageList.size()).isEqualTo(2);
        assertThat(ratioEachGameDTOList.get(1).getPercentage()).isEqualTo(97.54);
    }

    @Test
    public void getMonthlyEachPlatformRatioByPreviousYear() {
        // 통계 기반 정보 받아오기 : platformId, startDate, endDate
        MonthlyPlatformRequest request = new MonthlyPlatformRequest((long) 1, LocalDate.parse("2022-06-01"), LocalDate.parse("2022-06-30"));

        // 통계 기반 정보 유효성 체크
        request.isValidParam();
        Platform findPlatform = queryFactory
                .select(platform)
                .from(platform)
                .where(platform.id.eq(request.getPlatformId()))
                .fetchFirst();

        if (findPlatform == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_PLATFORM);
        }

        em.flush();
        em.clear();

        // 통계 데이터 가져오기
        // platform까지 같이 join 해야 함

        LocalDate now = LocalDate.now();
        StringTemplate formattedDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                account.purchaseDate,
                ConstantImpl.create("%Y-%m"));

        RangeDate rangeDateCondition = request.getRangeDateCondition();

        if (rangeDateCondition.equals(RangeDate.RANGE_DATE)) {
            List<MonthlyStaticsDTO> fetch4 = queryFactory
                    .select(new QMonthlyStaticsDTO(formattedDate, account.price.sum()))
                    .from(account)
                    .join(account.product, product)
                    .join(product.game, game)
                    .join(game.platform, platform)
                    .where(
                            account.purchaseDate.between(request.getStartDate(), request.getEndDate()),
                            platform.id.eq(request.getPlatformId())
                    )
                    .groupBy(formattedDate)
                    .orderBy(formattedDate.asc())
                    .fetch();
        }

        List<MonthlyStaticsDTO> fetch1 = queryFactory
                .select(new QMonthlyStaticsDTO(formattedDate, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .join(game.platform, platform)
                .where(
                        account.purchaseDate.between(now.minusYears(1), now),
                        platform.id.eq(request.getPlatformId())
                )
                .groupBy(formattedDate)
                .orderBy(formattedDate.asc())
                .fetch();

        List<MonthlyStaticsDTO> fetch2 = queryFactory
                .select(new QMonthlyStaticsDTO(formattedDate, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .join(game.platform, platform)
                .where(
                        account.purchaseDate.between(request.getStartDate(), request.getStartDate().plusYears(1)),
                        platform.id.eq(request.getPlatformId())
                )
                .groupBy(formattedDate)
                .orderBy(formattedDate.asc())
                .fetch();

        List<MonthlyStaticsDTO> fetch3 = queryFactory
                .select(new QMonthlyStaticsDTO(formattedDate, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .join(game.platform, platform)
                .where(
                        account.purchaseDate.between(request.getEndDate().minusYears(1), request.getEndDate()),
                        platform.id.eq(request.getPlatformId())
                )
                .groupBy(formattedDate)
                .orderBy(formattedDate.asc())
                .fetch();

        List<MonthlyStaticsDTO> fetch4 = queryFactory
                .select(new QMonthlyStaticsDTO(formattedDate, account.price.sum()))
                .from(account)
                .join(account.product, product)
                .join(product.game, game)
                .join(game.platform, platform)
                .where(
                        account.purchaseDate.between(request.getStartDate(), request.getEndDate()),
                        platform.id.eq(request.getPlatformId())
                )
                .groupBy(formattedDate)
                .orderBy(formattedDate.asc())
                .fetch();

        List<MonthlyRatioDTO> ratioList = new ArrayList<>();

        // 전 달 대비 증감값 목록 구하기
        for (int i = 0; i < fetch1.size(); i++) {
            if (i == 0) {
                continue;
            }

            double currentPrice = fetch1.get(i).getStaticValue();
            double previousPrice = fetch1.get(i - 1).getStaticValue();

            // 비율 구하기 -> 소수 2번째자리까지로 반올림
            double ratio = Math.round((currentPrice - previousPrice) / previousPrice * 100.0 * 100.0) / 100.0;
            ratioList.add(new MonthlyRatioDTO(fetch1.get(i).getMonth(), ratio));
        }

        //변환한 값 넣어서 리턴 처리 -> Response DTO 통해서 전달

        assertThat(ratioList.size()).isEqualTo(fetch1.size() - 1);
    }

    @Test
    public void getMonthlyEachGameRatioByPreviousMonth() {
        //request 받기
        MonthlyGameRequest request = new MonthlyGameRequest((long) 3, LocalDate.parse("2022-06-01"), LocalDate.parse("2022-06-30"));

        //request 유효성 검사
        request.isValidParam();
        Game findGame = queryFactory
                .select(game)
                .from(game)
                .where(game.id.eq(request.getGameId()))
                .fetchFirst();

        if (findGame == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_GAME);
        }

        // 통계 데이터 구하기
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

        List<MonthlyRatioDTO> ratioList = new ArrayList<>();

        // 전 달 대비 증감값 목록 구하기
        for (int i = 0; i < fetch1.size(); i++) {
            if (i == 0) {
                continue;
            }

            double currentPrice = fetch1.get(i).getStaticValue();
            double previousPrice = fetch1.get(i - 1).getStaticValue();

            // 비율 구하기 -> 소수 2번째자리까지로 반올림
            double ratio = Math.round((currentPrice - previousPrice) / previousPrice * 100.0 * 100.0) / 100.0;
            ratioList.add(new MonthlyRatioDTO(fetch1.get(i).getMonth(), ratio));
        }

        //변환한 값 넣어서 리턴 처리 -> Response DTO 통해서 전달
        //TODO

        assertThat(ratioList.size()).isEqualTo(fetch1.size() - 1);
    }

    private double totalPrice(List<GamePriceDTO> priceDTOList) {
        return priceDTOList.stream().mapToInt(GamePriceDTO::getPrice).sum();
    }

}