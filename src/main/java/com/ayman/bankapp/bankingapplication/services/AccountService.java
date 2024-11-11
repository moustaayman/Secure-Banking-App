package com.ayman.bankapp.bankingapplication.services;

import com.ayman.bankapp.bankingapplication.dtos.AccountBalanceResponse;
import com.ayman.bankapp.bankingapplication.dtos.AccountResponse;

import java.util.List;

public interface AccountService {
    AccountResponse createAccount(String userId, String accountName);
    AccountBalanceResponse getAccountBalance(String accountNumber);
    AccountResponse closeAccount(String accountNumber);
}
