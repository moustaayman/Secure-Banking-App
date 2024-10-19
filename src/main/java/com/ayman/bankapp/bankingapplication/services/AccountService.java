package com.ayman.bankapp.bankingapplication.services;

import com.ayman.bankapp.bankingapplication.dtos.BankResponse;

public interface AccountService {
    BankResponse createAccount(String email, String accountName);
}
