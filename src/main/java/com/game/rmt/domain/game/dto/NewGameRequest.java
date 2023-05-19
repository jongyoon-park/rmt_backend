package com.game.rmt.domain.game.dto;

import com.game.rmt.global.errorhandler.exception.BadRequestException;
import com.game.rmt.global.errorhandler.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewGameRequest {
    private Long platformId;
    private String gameName;

    public void isValidParam() {
        if (isNotNull(platformId) && isValidGameName()) {
            return;
        }

        throw new BadRequestException(ErrorCode.BAD_REQUEST_CREATE_GAME);
    }

    private boolean isNotNull(Object value) {
        return value != null;
    }

    private boolean isValidGameName() {
        return StringUtils.hasText(gameName);
    }
}
