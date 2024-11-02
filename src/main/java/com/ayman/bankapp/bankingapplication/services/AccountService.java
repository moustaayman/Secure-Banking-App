package com.ayman.bankapp.bankingapplication.services;

import com.ayman.bankapp.bankingapplication.dtos.AccountBalanceResponse;
import com.ayman.bankapp.bankingapplication.dtos.AccountResponse;

public interface AccountService {
    AccountResponse createAccount(String email, String accountName);
    AccountBalanceResponse getAccountBalance(String accountNumber);
    AccountResponse closeAccount(String accountNumber);
}
