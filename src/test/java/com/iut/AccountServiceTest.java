package com.iut;

import com.iut.account.model.Account;
import com.iut.account.service.AccountService;
import com.iut.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceTest {

    private Repository<Account, String> repository;
    
    private AccountService accountService;

    @BeforeEach
    void setup() {
        repository = new FakeAccountRepository();
        accountService = new AccountService(repository);
    }

    @Test
    void createAccountTest() {
        boolean created = accountService.createAccount("acc1", 1000, "user1");
        assertTrue(created);
        Account acc = repository.findById("acc1");
        assertNotNull(acc);
        assertEquals(1000, acc.getBalance());
        assertEquals("user1", acc.getUserId()); 
    }

    @Test
    void depositTest() {
        accountService.createAccount("acc1", 1000, "user1");
        accountService.deposit("acc1", 500);
        Account acc = repository.findById("acc1");
        assertEquals(1500, acc.getBalance());
    }

    @Test
    void withdrawTest() {
        accountService.createAccount("acc1", 1000, "user1");
        accountService.withdraw("acc1", 400);
        Account acc = repository.findById("acc1");
        assertEquals(600, acc.getBalance());
    }

    @Test
    void transferTest() {
        accountService.createAccount("acc1", 1000, "user1");
        accountService.createAccount("acc2", 200, "user2");
        boolean result = accountService.transfer("acc1", "acc2", 300);
        assertTrue(result);
        assertEquals(700, repository.findById("acc1").getBalance());
        assertEquals(500, repository.findById("acc2").getBalance());
    }

    @Test
    void getBalanceTest() {
        accountService.createAccount("acc1", 1000, "user1");
        int balance = accountService.getBalance("acc1");
        assertEquals(1000, balance);
    }

    @Test
    void existsAndGetAccountTest() {
        accountService.createAccount("acc1", 1000, "user1");
        assertTrue(repository.existsById("acc1"));
        Account acc = accountService.getAccount("acc1");
        assertNotNull(acc);
    }

    @Test 
    void getAllAccountsTest() {
        accountService.createAccount("acc1", 1000, "user1");
        accountService.createAccount("acc2", 500, "user2");
        List<Account> all = repository.findAll();
        assertEquals(2, all.size());
    }

    static class FakeAccountRepository implements Repository<Account, String> {
        private final Map<String, Account> storage = new HashMap<>();

        @Override
        public boolean save(Account input) {
            if (storage.containsKey(input.getId())) return false;
            storage.put(input.getId(), input);
            return true;
        }

        @Override
        public boolean update(Account input) {
            if (!storage.containsKey(input.getId())) return false;
            storage.put(input.getId(), input);
            return true;
        }

        @Override
        public boolean delete(String id) {
            return storage.remove(id) != null;
        }

        @Override
        public boolean existsById(String id) {
            return storage.containsKey(id);
        }

        @Override
        public Account findById(String id) {
            return storage.get(id);
        }

        @Override
        public List<Account> findAll() {
            return new ArrayList<>(storage.values());
        }
    }

}
