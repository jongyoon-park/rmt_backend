package com.game.rmt.domain.account.repository;

import com.game.rmt.domain.account.domain.Account;
import com.game.rmt.domain.account.repository.custom.AccountRepositoryCustom;
import com.game.rmt.domain.statistics.repository.custom.StaticsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom, StaticsRepositoryCustom {
}
