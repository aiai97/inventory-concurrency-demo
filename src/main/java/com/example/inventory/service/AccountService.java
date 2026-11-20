package com.example.inventory.service;

import com.example.inventory.domain.Account;
import com.example.inventory.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional(readOnly = true)
    public void testFirstLevelCache(Long accountId) {

        Account account1 =
                accountRepository.findById(accountId).orElseThrow();

        Account account2 =
                accountRepository.findById(accountId).orElseThrow();

        System.out.println(account1 == account2);
    }
}