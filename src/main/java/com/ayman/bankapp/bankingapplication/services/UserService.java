package com.ayman.bankapp.bankingapplication.services;

import com.ayman.bankapp.bankingapplication.dtos.UserRegistrationRequest;
import com.ayman.bankapp.bankingapplication.dtos.UserResponse;
import com.ayman.bankapp.bankingapplication.entities.Account;

import java.util.List;

public interface UserService {
    UserResponse registerUser(UserRegistrationRequest userRegistrationRequest);
    List<Account> getUserAccounts(String userId);
}
