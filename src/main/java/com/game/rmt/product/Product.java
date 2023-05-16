package com.game.rmt.product;

import com.game.rmt.game.Game;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity(name = "product")
@Getter
@NoArgsConstructor(access = PROTECTED)
@ToString(of = {"id", "name"})
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    private String productName;
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    public Product(String productName, Game game) {
        this.productName = productName;
        this.createDate = LocalDateTime.now();

        if (game != null) {
            changeGame(game);
        }
    }

    private void changeGame(Game game) {
        this.game = game;
        game.getProducts().add(this);
    }

}
