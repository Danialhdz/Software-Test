package com.iut;

import com.iut.account.model.Account;
import com.iut.account.repo.AccountRepositoryInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeAccountRepository implements AccountRepositoryInterface {

    private final Map<String, Account> data = new HashMap<>();

    @Override
    public boolean save(Account input) {
        data.put(input.getId(), input);
        return true;
    }

    @Override
    public boolean update(Account input) {
        if (!data.containsKey(input.getId()))
            return false;
        data.put(input.getId(), input);
        return true;
    }

    @Override
    public boolean delete(String s) {
        if (!data.containsKey(s))
            return false;
        data.remove(s);
        return true;
    }

    @Override
    public boolean existsById(String s) {
        return data.containsKey(s);
    }

    @Override
    public Account findById(String s) {
        return data.get(s);
    }

    @Override
    public List<Account> findAll() {
        return data.values().stream()
                .toList();
    }

    @Override
    public List<Account> findByUserId(String userId) {
        return data.values().stream()
                .filter(input -> input.getUserId().equals(userId))
                .toList();
    }
}
