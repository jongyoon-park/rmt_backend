package com.game.rmt.domain.platform.repository.custom;

import com.game.rmt.domain.platform.domain.Platform;

import java.util.List;

public interface PlatformRepositoryCustom {
    Platform findPlatformById(Long id);
    List<Platform> findPlatformByIds(List<Long> ids);
}
