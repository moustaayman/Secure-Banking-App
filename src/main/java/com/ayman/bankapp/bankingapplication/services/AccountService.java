package com.ayman.bankapp.bankingapplication.services;

import com.ayman.bankapp.bankingapplication.dtos.AccountResponse;

public interface AccountService {
    AccountResponse createAccount(String email, String accountName);
}
