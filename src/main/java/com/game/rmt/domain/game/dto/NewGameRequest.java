package com.game.rmt.domain.game.dto;

import com.game.rmt.global.errorhandler.exception.BadRequestException;
import com.game.rmt.global.errorhandler.exception.ErrorCode;
import com.game.rmt.global.parent.CreateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewGameRequest implements CreateRequest {
    private Long platformId;
    private String gameName;

    @Override
    public void isValidParam() {
        if (isNotNull(platformId) && isValidGameName()) {
            return;
        }

        throw new BadRequestException(ErrorCode.BAD_REQUEST_CREATE_GAME);
    }

    private boolean isValidGameName() {
        return isValidString(gameName);
    }
}
