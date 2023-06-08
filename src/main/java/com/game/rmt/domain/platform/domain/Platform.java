package com.game.rmt.domain.platform.domain;

import com.game.rmt.domain.game.domain.Game;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity(name = "platform")
@Getter
@NoArgsConstructor(access = PROTECTED)
@ToString(of = {"id", "name"})
public class Platform {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "platform")
    private List<Game> games = new ArrayList<>();

    public Platform(String name) {
        this.name = name;
    }


}
