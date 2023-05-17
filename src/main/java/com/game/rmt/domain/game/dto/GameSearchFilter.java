package com.game.rmt.domain.game.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameSearchFilter {
    private String searchGameName;
    private Long searchPlatformId;

    public GameSearchFilter(String searchGameName) {
        this.searchGameName = searchGameName;
    }

    public GameSearchFilter(Long searchPlatformId) {
        this.searchPlatformId = searchPlatformId;
    }

    public boolean isValidSearchPlatformId() {
        return isNotNull(searchPlatformId);
    }

    public boolean isValidSearchGameName() {
        return StringUtils.hasText(searchGameName);
    }

    private boolean isNotNull(Object value) {
        return value != null;
    }
}
