package com.game.rmt.domain.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class AccountListResponse {
    private Long totalCount;
    private List<AccountResponse> accountList;

    public AccountListResponse(Page<AccountResponse> accountPage) {
        this.totalCount = accountPage.getTotalElements();
        this.accountList = accountPage.getContent();
    }
}
