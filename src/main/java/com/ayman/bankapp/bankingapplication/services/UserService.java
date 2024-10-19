package com.ayman.bankapp.bankingapplication.services;

import com.ayman.bankapp.bankingapplication.dtos.BankResponse;
import com.ayman.bankapp.bankingapplication.dtos.UserRequest;

public interface UserService {
    BankResponse createUser(UserRequest userRequest);
}
