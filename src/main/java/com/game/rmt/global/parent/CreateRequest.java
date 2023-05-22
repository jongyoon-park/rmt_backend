package com.game.rmt.global.parent;

import org.springframework.util.StringUtils;

public interface CreateRequest {
    default boolean isNotNull(Object object) {
        return object != null;
    }
    default boolean isValidString(String value) {
        return StringUtils.hasText(value);
    }

    void isValidParam();
}
