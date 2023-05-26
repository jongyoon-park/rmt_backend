package com.game.rmt.domain.account.repository.custom;

import com.game.rmt.domain.account.domain.Account;
import com.game.rmt.domain.account.dto.AccountSearchFilter;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountRepositoryCustom {
    List<Account> findAccountsByFilter(AccountSearchFilter filter, Pageable pageable);
    JPAQuery<Account> findAccountsByFilterQuery(AccountSearchFilter filter);
    Account findAccountById(Long accountId);
}
