package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;

public interface AccountServices {
    public Account saveAccount(Account account);
    public Account findById(long id);
}
