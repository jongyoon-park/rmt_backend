package com.game.rmt.domain.account.controller;

import com.game.rmt.domain.account.dto.AccountListResponse;
import com.game.rmt.domain.account.dto.AccountResponse;
import com.game.rmt.domain.account.dto.AccountSearchFilter;
import com.game.rmt.domain.account.dto.NewAccountRequest;
import com.game.rmt.domain.account.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("rmt/account")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/list")
    public AccountListResponse getAccounts(@RequestBody AccountSearchFilter filter) {
        return new AccountListResponse(accountService.getAccounts(filter));
    }

    @PostMapping("")
    public AccountResponse createAccount(@RequestBody NewAccountRequest accountRequest) {
        return new AccountResponse(accountService.createAccount(accountRequest));
    }
}
