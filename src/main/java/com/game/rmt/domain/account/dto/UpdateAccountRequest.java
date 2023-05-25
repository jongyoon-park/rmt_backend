package com.game.rmt.domain.account.dto;

import com.game.rmt.global.errorhandler.exception.BadRequestException;
import com.game.rmt.global.errorhandler.exception.ErrorCode;
import com.game.rmt.global.parent.CreateRequest;

import java.time.LocalDate;

public class UpdateAccountRequest implements CreateRequest {
    private Long accountId;
    private Integer price;
    private LocalDate purchaseDate;
    private String note;

    @Override
    public void isValidParam() {
        if (isValidAccountId() && isValidPrice()) {
            return;
        }

        throw new BadRequestException(ErrorCode.BAD_REQUEST_UPDATE_ACCOUNT);
    }

    private boolean isValidAccountId() {
        return isNotNull(accountId);
    }

    private boolean isValidPrice() {
        return isNotNull(price) && price >= 0;
    }
}
