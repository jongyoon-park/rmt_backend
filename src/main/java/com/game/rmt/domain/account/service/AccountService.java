package com.game.rmt.domain.account.service;

import com.game.rmt.domain.account.domain.Account;
import com.game.rmt.domain.account.dto.*;
import com.game.rmt.domain.account.repository.AccountRepository;
import com.game.rmt.domain.product.domain.Product;
import com.game.rmt.domain.product.service.ProductService;
import com.game.rmt.global.errorhandler.exception.ErrorCode;
import com.game.rmt.global.errorhandler.exception.NotFoundException;
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

    private final ProductService productService;

    public Page<AccountResponse> getAccounts(AccountSearchFilter filter) {
        PageRequest pageable = getPageRequest(filter);
        List<Account> accountList = accountRepository.findAccountsByFilter(filter, pageable);
        return convertAccountResponsePage(convertAccountResponseList(accountList), filter, pageable);
    }

    public AccountDTO createAccount(NewAccountRequest newAccountRequest) {
        Product product = validateCreateAccount(newAccountRequest);

        Account account = new Account(
                newAccountRequest.getPrice(),
                newAccountRequest.getPurchaseDate(),
                newAccountRequest.getNote(),
                product);

        return new AccountDTO(accountRepository.save(account));
    }

    public void updateAccount(UpdateAccountRequest request) {
        Account account = validateUpdateAccount(request);
        updateAccountByRequest(account, request);
    }

    public Account getAccount(Long accountId) {
        Account account = accountRepository.findAccountById(accountId);

        if (account == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_ACCOUNT);
        }

        return account;
    }

    private Account validateUpdateAccount(UpdateAccountRequest request) {
        request.isValidParam();
        return getAccount(request.getAccountId());
    }

    private void updateAccountByRequest(Account account, UpdateAccountRequest request) {
        account.updateAccountByRequest(request.getPrice(), request.getPurchaseDate(), request.getNote());
        accountRepository.save(account);
    }

    private Product validateCreateAccount(NewAccountRequest newAccountRequest) {
        newAccountRequest.isValidParam();
        return productService.getProduct(newAccountRequest.getProductId());
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
