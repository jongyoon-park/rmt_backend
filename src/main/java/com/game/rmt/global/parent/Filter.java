package com.game.rmt.global.parent;

import org.springframework.util.StringUtils;

public interface Filter {
    default boolean isNotNull(Object object) {
        return object != null;
    }

    default boolean isValidString(String value) {
        return StringUtils.hasText(value);
    }
}
