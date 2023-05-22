package com.game.rmt.domain.product.service;

import com.game.rmt.domain.account.domain.Account;
import com.game.rmt.domain.game.domain.Game;
import com.game.rmt.domain.game.repository.GameRepository;
import com.game.rmt.domain.platform.domain.Platform;
import com.game.rmt.domain.platform.repository.PlatformRepository;
import com.game.rmt.domain.platform.service.PlatformService;
import com.game.rmt.domain.product.domain.Product;
import com.game.rmt.domain.product.dto.NewProductRequest;
import com.game.rmt.domain.product.dto.ProductDTO;
import com.game.rmt.domain.product.dto.ProductSearchFilter;
import com.game.rmt.domain.product.repository.ProductRepository;
import com.game.rmt.global.errorhandler.exception.BadRequestException;
import com.game.rmt.global.errorhandler.exception.ErrorCode;
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
import java.util.List;

import static com.game.rmt.domain.game.domain.QGame.game;
import static com.game.rmt.domain.platform.domain.QPlatform.platform;
import static com.game.rmt.domain.product.domain.QProduct.product;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Commit
class ProductServiceTest {
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
    public void getProducts() {
        em.flush();
        em.clear();

        ProductSearchFilter filter = new ProductSearchFilter();

        List<Product> products = queryFactory
                .selectFrom(product)
                .join(product.game, game)
                .fetchJoin()
                .join(game.platform, platform)
                .fetchJoin()
//                .where(platform.id.eq((long) 1), game.id.eq((long) 2), product.isActivated.eq(true))
                .where(platform.id.eq((long) 1))
                .fetch();

        assertThat(products.size()).isEqualTo(2);
    }

    @Test
    public void createProduct() {
        //받아야할 정보 : platformId, gameId, productName
        NewProductRequest request = new NewProductRequest((long) 1, (long) 4, "7500 Jewel");
        request.isValidParam();

        //platform 유효성 체크 -> platform 조회되지 않으면 반려
        Platform findPlatform = platformService.getPlatform(request.getPlatformId());

        if (findPlatform == null) {
            throw new BadRequestException(ErrorCode.NOT_FOUND_PLATFORM);
        }

        //game 유효성 체크 -> game 조회되지 않으면 반려
        Game findGame = gameRepository.findGameById(request.getGameId());

        if (findGame == null) {
            throw new BadRequestException(ErrorCode.NOT_FOUND_GAME);
        }

        //game의 platformId와 request의 platformId의 값이 일치하는지 체크
        if (findGame.getPlatform().getId() != request.getPlatformId()) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        //이름 중복 여부 체크 (gameId, productName)
        Product findProduct = productRepository.findProductByGameIdAndProductName(request.getGameId(), "");

        //중복 존재하면 반려
        if (findProduct != null) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST_CREATE_PRODUCT);
        }

        //유효성 체크를 통과하면 product entity 생성
        Product product = new Product(request.getProductName(), findGame);
        //save를 통해 저장, 저장 후 ProductDTO 리턴
        ProductDTO save = new ProductDTO(productRepository.save(product));

        assertThat(save.getProductName()).isEqualTo(request.getProductName());


    }
}