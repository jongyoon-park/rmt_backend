package com.game.rmt.domain.account.service;

import com.game.rmt.domain.account.domain.Account;
import com.game.rmt.domain.account.dto.AccountResponse;
import com.game.rmt.domain.account.dto.AccountSearchFilter;
import com.game.rmt.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Page<AccountResponse> getAccounts(AccountSearchFilter filter) {
        PageRequest pageable = getPageRequest(filter);
        List<Account> accountList = accountRepository.findAccountsByFilter(filter, pageable);
        return convertAccountResponsePage(convertAccountResponseList(accountList), filter, pageable);
    }

    private Page<AccountResponse> convertAccountResponsePage(List<AccountResponse> accountPage, AccountSearchFilter filter, PageRequest pageable) {
        return PageableExecutionUtils.getPage(
                accountPage,
                pageable,
                () -> accountRepository.findAccountsByFilterQuery(filter).fetch().size()
        );
    }

    private List<AccountResponse> convertAccountResponseList(List<Account> accountList) {
        List<AccountResponse> accountResponseList = new ArrayList<>();

        if (accountList == null || accountList.isEmpty()) {
            return accountResponseList;
        }

        accountList.forEach(account -> accountResponseList.add(new AccountResponse(account)));

        return accountResponseList;
    }

    private PageRequest getPageRequest(AccountSearchFilter filter) {
        validatePagination(filter);
        return PageRequest.of(filter.getSearchPage(), filter.getSearchLimit());
    }

    private void validatePagination(AccountSearchFilter filter) {
        filter.validateLimit();
        filter.validatePage();
    }
}
