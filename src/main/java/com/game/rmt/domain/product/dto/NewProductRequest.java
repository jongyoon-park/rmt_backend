package com.game.rmt.domain.product.dto;

import com.game.rmt.global.errorhandler.exception.BadRequestException;
import com.game.rmt.global.errorhandler.exception.ErrorCode;
import com.game.rmt.global.parent.CreateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewProductRequest implements CreateRequest {
    private Long platformId;
    private Long gameId;
    private String productName;

    @Override
    public void isValidParam() {
        if (isNotNull(platformId) && isNotNull(gameId) && isValidProductName()) {
            return;
        }

        throw new BadRequestException(ErrorCode.BAD_REQUEST_CREATE_PRODUCT);
    }

    private boolean isValidProductName() {
        return isValidString(productName);
    }
}
