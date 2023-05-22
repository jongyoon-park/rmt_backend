package com.game.rmt.domain.product.dto;

import com.game.rmt.global.parent.Filter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchFilter implements Filter {
    private Long searchPlatformId;
    private Long searchGameId;
    private Boolean searchIsActivated;

    public boolean isValidSearchPlatformId() {
        return isNotNull(searchPlatformId);
    }

    public boolean isValidSearchGameId() {
        return isNotNull(searchGameId);
    }

    public boolean isValidSearchIsActivated() {
        return isNotNull(searchIsActivated);
    }
}
