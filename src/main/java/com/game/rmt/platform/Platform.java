package com.game.rmt.platform;

import com.game.rmt.game.Game;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity(name = "platform")
@Getter
@NoArgsConstructor(access = PROTECTED)
@ToString(of = {"id", "name"})
public class Platform {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(mappedBy = "platform")
    private List<Game> games = new ArrayList<>();

    public Platform(String name) {
        this.name = name;
    }


}
