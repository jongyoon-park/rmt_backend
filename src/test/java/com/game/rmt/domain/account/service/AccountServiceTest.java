package com.game.rmt.domain.account.service;

import com.game.rmt.domain.account.domain.Account;
import com.game.rmt.domain.account.domain.QAccount;
import com.game.rmt.domain.account.dto.*;
import com.game.rmt.domain.account.repository.AccountRepository;
import com.game.rmt.domain.game.domain.Game;
import com.game.rmt.domain.game.repository.GameRepository;
import com.game.rmt.domain.platform.domain.Platform;
import com.game.rmt.domain.platform.repository.PlatformRepository;
import com.game.rmt.domain.platform.service.PlatformService;
import com.game.rmt.domain.product.domain.Product;
import com.game.rmt.domain.product.repository.ProductRepository;
import com.game.rmt.global.errorhandler.exception.ErrorCode;
import com.game.rmt.global.errorhandler.exception.NotFoundException;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.test.annotation.Commit;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.game.rmt.domain.account.domain.QAccount.account;
import static com.game.rmt.domain.game.domain.QGame.game;
import static com.game.rmt.domain.platform.domain.QPlatform.platform;
import static com.game.rmt.domain.product.domain.QProduct.product;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Commit
class AccountServiceTest {
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
    public void getAccounts() {
        // filter 생성
        AccountSearchFilter filter = new AccountSearchFilter(
                LocalDate.parse("2022-08-17"),
                LocalDate.parse("2023-08-17"),
                null,
                null,
                null,
                null,
                null,
                10,
                0
        );
        // pageable 생성
        PageRequest pageable = PageRequest.of(filter.getSearchPage(), filter.getSearchLimit());
        // repository에서 filter에 따라 데이터 검색
        List<Account> accountList = queryFactory
                .selectFrom(account)
                .join(account.product, product)
                .fetchJoin()
                .join(product.game, game)
                .fetchJoin()
                .join(game.platform, platform)
                .fetchJoin()
                .where(
                        account.purchaseDate.between(null, filter.getSearchEndDate())
                        /*account.price.between(filter.getMinPrice(), filter.getMaxPrice()),
                        product.productName.contains(filter.getProductName()),
                        game.id.in(filter.getGameIdList()),
                        platform.id.in(filter.getPlatformIdList())*/
                )
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
        // 검색한 데이터를 DTO로 변환하기
        List<AccountResponse> accountResponseList = new ArrayList<>();
        if (accountList.size() > 0) {
            accountList.forEach(account -> accountResponseList.add(new AccountResponse(account)));
        }
        // 조건에 맞는 전체 데이터를 가져오는 count 쿼리 함수 생성
        JPAQuery<Account> countQuery = queryFactory
                .selectFrom(account)
                .join(account.product, product)
                .fetchJoin()
                .join(product.game, game)
                .fetchJoin()
                .join(game.platform, platform)
                .fetchJoin()
                .where(
                        account.purchaseDate.between(null, filter.getSearchEndDate())
                        /*account.price.between(filter.getMinPrice(), filter.getMaxPrice()),
                        product.productName.contains(filter.getProductName()),
                        game.id.in(filter.getGameIdList()),
                        platform.id.in(filter.getPlatformIdList())*/
                );


        // PageableExecutionUtils.getPage()로 페이징 처리
        Page<AccountResponse> page = PageableExecutionUtils.getPage(accountResponseList, pageable, () -> countQuery.fetch().size());

        assertThat(page.getTotalElements()).isEqualTo(2);
    }

    @Test
    public void insertAccount() {
        //신규 account 정보 받기
        NewAccountRequest request = new NewAccountRequest((long) 5, 100000, LocalDate.now(), null);
        //poductId 유효성 체크

        Product findProduct = productRepository.findProductById(request.getProductId());

        if (findProduct == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_PRODUCT);
        }
        //account entity 생성
        Account account = new Account(request.getPrice(), request.getPurchaseDate(), findProduct);
        //accountRepository.save 실행
        accountRepository.save(account);
        //실행 결과 accountResponse로 변환
        AccountResponse response = new AccountResponse(account);

        assertThat(response.getPrice()).isEqualTo(100000);
    }

    @Test
    public void updateAccount() {
        // account 수정 시 받을 정보 : purchaseDate, price, note
        UpdateAccountRequest request = new UpdateAccountRequest((long) 8, 0, LocalDate.parse("2023-05-22"), "add note");
        request.isValidParam();
        // AccountId 유효성 체크
        Account findAccount = queryFactory
                .selectFrom(account)
                .where(account.id.eq(request.getAccountId()))
                .fetchFirst();

        if (findAccount == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_ACCOUNT);
        }

        // 유효한 정보가 들어온 경우 account 엔티티의 값 변경 -> 이 변경하는 코드가 entity에 들어갈 경우, entity는 UpdateAccountRequest에 의존하게 됨
        findAccount.updateAccountByRequest(request.getPrice(), request.getPurchaseDate(), request.getNote());
        // 변경한 값을 accountRepository.save로 저장
        AccountDTO save = new AccountDTO(accountRepository.save(findAccount));

        em.flush();
        em.clear();
        // 저장한 account를 accountResponse로 변환
        Account accountById = accountRepository.findAccountById(request.getAccountId());
        assertThat(accountById.getProduct()).isNotNull();
    }
}