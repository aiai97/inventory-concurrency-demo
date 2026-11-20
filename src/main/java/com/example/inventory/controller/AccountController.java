package com.example.inventory.controller;

import com.example.inventory.service.AccountService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;

    }
    @PostMapping("/testLevelCache")
    public void testFirstLevelCache(@RequestParam Long accountId) {
        accountService.testFirstLevelCache(accountId);
    }

}
