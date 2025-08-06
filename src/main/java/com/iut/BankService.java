package com.iut;

import com.iut.account.model.Account;
import com.iut.account.service.AccountService;
import com.iut.user.model.User;
import com.iut.user.service.UserService;

import java.util.List;
import java.util.UUID;

public class BankService {

    private final UserService userService;
    private final AccountService accountService;

    public BankService(final UserService userService, final AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    public boolean registerNewUser(User user) {
        boolean created = userService.createUser(user);
        if (created) {
            String defaultAccountId = UUID.randomUUID().toString();
            return accountService.createAccount(defaultAccountId, 0, user.getId());
        }
        return false;
    }

    public List<Account> getUserAccounts(String userId) {
        return accountService.getUserAccounts(userId);
    }

    public User getUser(String userId) {
        return userService.getUser(userId);
    }

    public boolean addAccountToUser(String userId, Account account) {
        account.setUserId(userId);
        return accountService.createAccount(account.getId(), account.getBalance(), userId);
    }

    public Account getAccount(String accountId) {
        return accountService.getAccount(accountId);
    }

    public boolean deleteAccount(String accountId) {
        return accountService.deleteAccount(accountId);
    }
}
