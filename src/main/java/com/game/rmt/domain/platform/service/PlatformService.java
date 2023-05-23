package com.game.rmt.domain.platform.service;

import com.game.rmt.domain.platform.domain.Platform;
import com.game.rmt.domain.platform.dto.PlatformDTO;
import com.game.rmt.domain.platform.repository.PlatformRepository;
import com.game.rmt.global.errorhandler.exception.ErrorCode;
import com.game.rmt.global.errorhandler.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlatformService {

    private final PlatformRepository platformRepository;

    public List<PlatformDTO> getPlatforms() {
        List<Platform> platformList = platformRepository.findAll();

        if (platformList == null || platformList.isEmpty()) {
            return new ArrayList<>();
        }

        return convertPlatformDTOList(platformList);
    }

    public Platform getPlatform(Long id) {
        Platform platform = platformRepository.findPlatformById(id);

        if (platform == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_PLATFORM);
        }

        return platform;
    }

    private List<PlatformDTO> convertPlatformDTOList(List<Platform> platformList) {
        List<PlatformDTO> platformDTOList = new ArrayList<>();

        for (Platform platform : platformList) {
            platformDTOList.add(new PlatformDTO(platform));
        }

        return platformDTOList;
    }
}
