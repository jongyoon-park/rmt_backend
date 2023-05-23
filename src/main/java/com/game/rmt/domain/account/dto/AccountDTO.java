package com.game.rmt.domain.account.dto;

import com.game.rmt.domain.game.dto.GameDTO;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AccountDTO {
    private Long accountId;
    private GameDTO game;
    private Integer price;
    private LocalDate purchaseDate;
    private String note;
}
