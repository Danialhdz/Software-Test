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

    /**
     * ثبت کاربر جدید به همراه ساخت یک حساب پیش‌فرض با موجودی ۰
     */
    public boolean registerNewUser(User user) {
        boolean created = userService.createUser(user);
        if (created) {
            String defaultAccountId = UUID.randomUUID().toString();
            return accountService.createAccount(defaultAccountId, 0, user.getId());
        }
        return false;
    }

    /**
     * دریافت حساب‌های یک کاربر بر اساس userId
     */
    public List<Account> getUserAccounts(String userId) {
        return accountService.getUserAccounts(userId);
    }

    /**
     * دریافت اطلاعات یک کاربر بر اساس userId
     */
    public User getUser(String userId) {
        return userService.getUser(userId);
    }

    /**
     * افزودن حساب جدید برای یک کاربر خاص
     */
    public boolean addAccountToUser(String userId, Account account) {
        account.setUserId(userId); // تضمین انتساب به کاربر صحیح
        return accountService.createAccount(account.getId(), account.getBalance(), userId);
    }

    /**
     * دریافت اطلاعات حساب بر اساس accountId
     */
    public Account getAccount(String accountId) {
        return accountService.getAccount(accountId);
    }

    /**
     * حذف حساب با توجه به شناسه حساب
     */
    public boolean deleteAccount(String accountId) {
        return accountService.deleteAccount(accountId);
    }
}
