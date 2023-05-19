package com.game.rmt.domain.platform.repository;

import com.game.rmt.domain.platform.domain.Platform;
import com.game.rmt.domain.platform.repository.custom.PlatformRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformRepository extends JpaRepository<Platform, Long>, PlatformRepositoryCustom {
}
