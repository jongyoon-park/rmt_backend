package com.game.rmt.domain.platform.repository.custom;

import com.game.rmt.domain.platform.domain.Platform;

public interface PlatformRepositoryCustom {
    Platform findPlatformById(Long id);
}
