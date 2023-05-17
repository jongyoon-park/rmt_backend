package com.game.rmt.domain.game.repository;

import com.game.rmt.domain.game.domain.Game;
import com.game.rmt.domain.game.repository.custom.GameRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long>, GameRepositoryCustom {

}
