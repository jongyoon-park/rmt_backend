package com.game.rmt.domain.account.dto;

import com.game.rmt.global.errorhandler.exception.BadRequestException;
import com.game.rmt.global.errorhandler.exception.ErrorCode;
import com.game.rmt.global.parent.CreateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class NewAccountRequest implements CreateRequest {
    private Long productId;
    private Integer price;
    private LocalDate purchaseDate;
    private String note;

    @Override
    public void isValidParam() {
        if (isValidProductId() && isValidPrice() && isValidPurchaseDate()) {
            return;
        }

        throw new BadRequestException(ErrorCode.BAD_REQUEST_CREATE_ACCOUNT);
    }

    private boolean isValidPrice() {
        return isNotNull(price) && price > 0;
    }

    private boolean isValidPurchaseDate() {
        return isNotNull(purchaseDate);
    }

    private boolean isValidProductId() {
        return isNotNull(productId);
    }
}
