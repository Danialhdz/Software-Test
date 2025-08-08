package com.iut;

import com.iut.account.repo.AccountRepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.iut.account.model.Account;
import com.iut.account.service.AccountService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceTest {

    private AccountRepositoryInterface repository;
    
    private AccountService accountService;

    @BeforeEach
    void setup() {
        repository = new FakeAccountRepository();
        accountService = new AccountService(repository);
    }

    @Test
    void createAccountTest() {
        boolean created = accountService.createAccount("a1", 1000, "u1");
        assertTrue(created);

        Account acc = repository.findById("a1");
        assertNotNull(acc);
        assertEquals(1000, acc.getBalance());
        assertEquals("u1", acc.getUserId());
    }

    @Test
    void depositTest() {
        accountService.createAccount("a2", 500, "u2");
        boolean result = accountService.deposit("a2", 200);
        assertTrue(result);

        Account acc = repository.findById("a2");
        assertEquals(700, acc.getBalance());
    }

    @Test
    void withdrawTest() {
        accountService.createAccount("a3", 1000, "u3");
        boolean result = accountService.withdraw("a3", 300);
        assertTrue(result);

        Account acc = repository.findById("a3");
        assertEquals(700, acc.getBalance());
    }

    @Test
    void transferTest() {
        accountService.createAccount("a5", 1000, "u5");
        accountService.createAccount("a6", 500, "u6");

        boolean result = accountService.transfer("a5", "a6", 400);
        assertTrue(result);

        assertEquals(600, accountService.getBalance("a5"));
        assertEquals(900, accountService.getBalance("a6"));
    }

    @Test
    void getBalanceTest() {
        accountService.createAccount("a7", 1500, "u7");
        int balance = accountService.getBalance("a7");
        assertEquals(1500, balance);
    }

    @Test
    void existsAndGetAccountTest() {
        accountService.createAccount("a8", 800, "u8");

        assertTrue(repository.existsById("a8"));
        Account acc = accountService.getAccount("a8");
        assertNotNull(acc);
        assertEquals("u8", acc.getUserId());
    }

    @Test
    void getAllAccountsTest() {
        accountService.createAccount("a9", 100, "u9");
        accountService.createAccount("a10", 200, "u9");

        List<Account> accounts = accountService.getUserAccounts("u9");
        assertEquals(2, accounts.size());
    }

}
