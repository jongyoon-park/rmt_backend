package com.game.rmt.domain.account.dto;

import com.game.rmt.domain.account.domain.Account;
import com.game.rmt.domain.game.dto.GameDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private Long accountId;
    private GameDTO game;
    private Integer price;
    private LocalDate purchaseDate;
    private String note;

    public AccountDTO(Account account) {
        this.accountId = account.getId();
        this.game = new GameDTO(account.getProduct().getGame());
        this.price = account.getPrice();
        this.purchaseDate = account.getPurchaseDate();
        this.note = account.getNote();
    }
}
