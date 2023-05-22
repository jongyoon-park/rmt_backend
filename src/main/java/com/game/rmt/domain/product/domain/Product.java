package com.game.rmt.domain.product.domain;

import com.game.rmt.domain.account.domain.Account;
import com.game.rmt.domain.game.domain.Game;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity(name = "product")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    private String productName;
    private LocalDateTime createDate;
    private boolean isActivated;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "product")
    private List<Account> accounts = new ArrayList<>();

    public Product(String productName, Game game) {
        this.productName = productName;
        this.createDate = LocalDateTime.now();
        this.isActivated = true;

        if (game != null) {
            changeGame(game);
        }
    }

    private void changeGame(Game game) {
        this.game = game;
        game.getProducts().add(this);
    }

}
