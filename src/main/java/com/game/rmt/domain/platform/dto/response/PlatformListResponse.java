package com.game.rmt.domain.platform.dto.response;

import com.game.rmt.domain.platform.dto.PlatformDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlatformListResponse {
    private List<PlatformDTO> platformList;
}
