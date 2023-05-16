package com.game.rmt.domain.game.domain;

import com.game.rmt.domain.platform.domain.Platform;
import com.game.rmt.domain.product.domain.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Game {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(fetch = LAZY)
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
