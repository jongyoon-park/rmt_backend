package com.game.rmt.game;

import com.game.rmt.platform.Platform;
import com.game.rmt.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Game {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "platform_id")
    private Platform platform;

    @OneToMany
    private List<Product> products = new ArrayList<>();

    public Game(String name, Platform platform) {
        this.name = name;

        if (platform != null) {
            changePlatform(platform);
        }
    }

    private void changePlatform(Platform platform) {
        this.platform = platform;
        platform.getGames().add(this);
    }


}
