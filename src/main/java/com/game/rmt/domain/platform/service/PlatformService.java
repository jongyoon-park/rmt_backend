package com.game.rmt.domain.platform.service;

import com.game.rmt.domain.platform.domain.Platform;
import com.game.rmt.domain.platform.dto.PlatformDTO;
import com.game.rmt.domain.platform.repository.PlatformRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PlatformService {

    private final PlatformRepository platformRepository;

    public List<PlatformDTO> getPlatforms() {
        List<Platform> platformList = platformRepository.findAll();

        if (platformList == null) {
            return new ArrayList<>();
        }

        List<PlatformDTO> platformDTOList = new ArrayList<>();

        for (Platform platform : platformList) {
            platformDTOList.add(new PlatformDTO(platform));
        }

        return platformDTOList;
    }
}
