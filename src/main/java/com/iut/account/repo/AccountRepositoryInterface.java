package com.iut.account.repo;

import com.iut.Repository;
import com.iut.account.model.Account;

import java.util.List;

public interface AccountRepositoryInterface extends Repository<Account, String> {
    List<Account> findByUserId(String userId);
}
