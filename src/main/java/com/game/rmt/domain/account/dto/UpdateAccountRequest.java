package com.game.rmt.domain.account.dto;

import com.game.rmt.global.errorhandler.exception.BadRequestException;
import com.game.rmt.global.errorhandler.exception.ErrorCode;
import com.game.rmt.global.parent.CreateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountRequest implements CreateRequest {
    //update하지 않을 정보는 null로 전달, 단 accountId는 필수
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

    public boolean isValidPrice() {
        return !isNotNull(price) || price >= 0;
    }

    public boolean isValidPurchaseDate() {
        return isNotNull(purchaseDate);
    }

    public boolean isValidNote() {
        return isNotNull(note);
    }

    private boolean isValidAccountId() {
        return isNotNull(accountId);
    }
}
