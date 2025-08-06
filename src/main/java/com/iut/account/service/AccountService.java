package com.iut.account.service;

import com.iut.Repository;
import com.iut.account.model.Account;

import java.util.*;
import java.util.stream.Collectors;

public class AccountService {

    private final Repository<Account, String> repository;
    private final Map<String, Account> memoryStorage = new HashMap<>();

    public AccountService(Repository<Account, String> repository) {
        this.repository = repository;
    }

    public boolean createAccount(String id, int initialBalance, String userId) {
        if (repository.existsById(id)) {
            return false;
        }
        Account account = new Account(id, initialBalance);
        account.setUserId(userId);
        memoryStorage.put(id, account);
        return repository.save(account);
    }

    public boolean deposit(String accountId, int amount) {
        if (repository.existsById(accountId)) {
            Account account = repository.findById(accountId);
            account.setBalance(account.getBalance() + amount);
            memoryStorage.put(accountId, account);
            return repository.update(account);
        }
        return false;
    }

    public boolean withdraw(String accountId, int amount) {
        if (repository.existsById(accountId)) {
            Account account = repository.findById(accountId);
            if (account.getBalance() < amount) {
                throw new IllegalArgumentException("Insufficient funds");
            }
            account.setBalance(account.getBalance() - amount);
            memoryStorage.put(accountId, account);
            return repository.update(account);
        }
        return false;
    }

    public boolean transfer(String fromId, String toId, int amount) {
        if (repository.existsById(fromId) && repository.existsById(toId)) {
            Account from = repository.findById(fromId);
            Account to = repository.findById(toId);
            if (from.getBalance() < amount) {
                throw new IllegalArgumentException("Insufficient funds in source account");
            }
            from.setBalance(from.getBalance() - amount);
            to.setBalance(to.getBalance() + amount);
            memoryStorage.put(fromId, from);
            memoryStorage.put(toId, to);
            repository.update(from);
            repository.update(to);
            return true;
        }
        return false;
    }

    public int getBalance(String id) {
        if (repository.existsById(id)) {
            return repository.findById(id).getBalance();
        }
        throw new IllegalArgumentException("Account not found");
    }

    public boolean deleteAccount(final String id) {
        memoryStorage.remove(id);
        return repository.delete(id);
    }

    public Account getAccount(String id) {
        return repository.findById(id);
    }

    public List<Account> getUserAccounts(String userId) {
        return memoryStorage.values().stream()
                .filter(acc -> userId.equals(acc.getUserId()))
                .collect(Collectors.toList());
    }
}
