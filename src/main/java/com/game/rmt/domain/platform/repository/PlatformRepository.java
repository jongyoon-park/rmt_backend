package com.game.rmt.domain.platform.repository;

import com.game.rmt.domain.platform.domain.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformRepository extends JpaRepository<Platform, Long> {
}
