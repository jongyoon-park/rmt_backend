package com.game.rmt.domain.platform.dto;

import com.game.rmt.domain.platform.domain.Platform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlatformDTO {

    private Long id;
    private String platformName;

    public PlatformDTO (Platform platform) {
        this.id = platform.getId();
        this.platformName = platform.getName();
    }
}
