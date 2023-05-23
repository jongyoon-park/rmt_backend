package com.game.rmt.domain.account.controller;

import com.game.rmt.domain.account.dto.AccountListResponse;
import com.game.rmt.domain.account.dto.AccountSearchFilter;
import com.game.rmt.domain.account.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    public AccountListResponse getAccounts(AccountSearchFilter filter) {
        return new AccountListResponse(accountService.getAccounts(filter));
    }
}
