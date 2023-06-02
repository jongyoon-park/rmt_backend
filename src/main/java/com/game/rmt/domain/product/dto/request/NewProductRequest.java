package com.game.rmt.domain.product.dto.request;

import com.game.rmt.global.errorhandler.exception.BadRequestException;
import com.game.rmt.global.parent.CreateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.game.rmt.global.errorhandler.exception.ErrorCode.BAD_REQUEST_CREATE_PRODUCT;

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

        throw new BadRequestException(BAD_REQUEST_CREATE_PRODUCT);
    }

    private boolean isValidProductName() {
        return isValidString(productName);
    }
}
