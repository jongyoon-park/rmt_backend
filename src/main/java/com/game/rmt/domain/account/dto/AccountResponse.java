package com.game.rmt.domain.account.dto;

import com.game.rmt.domain.account.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class AccountResponse {
    private Long accountId;
    private String platformName;
    private String gameName;
    private Integer price;
    private LocalDate purchaseDate;
    private String note;

    public AccountResponse(AccountDTO accountDTO) {
        this.accountId = accountDTO.getAccountId();
        this.platformName = accountDTO.getGame().getPlatform().getPlatformName();
        this.gameName = accountDTO.getGame().getGameName();
        this.price = accountDTO.getPrice();
        this.purchaseDate = accountDTO.getPurchaseDate();
        this.note = accountDTO.getNote();
    }

    public AccountResponse(Account account) {
        this.accountId = account.getId();
        this.platformName = account.getProduct().getGame().getPlatform().getName();
        this.gameName = account.getProduct().getGame().getName();
        this.price = account.getPrice();
        this.purchaseDate = account.getPurchaseDate();
        this.note = account.getNote();
    }
}
