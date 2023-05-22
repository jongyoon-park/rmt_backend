package com.game.rmt.global.parent;

public interface Filter {
    default boolean isNotNull(Object object) {
        return object != null;
    }
}
