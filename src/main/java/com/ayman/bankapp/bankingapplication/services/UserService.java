package com.ayman.bankapp.bankingapplication.services;

import com.ayman.bankapp.bankingapplication.dtos.UserLoginRequest;
import com.ayman.bankapp.bankingapplication.dtos.UserLoginResponse;
import com.ayman.bankapp.bankingapplication.dtos.UserRegistrationRequest;
import com.ayman.bankapp.bankingapplication.dtos.UserResponse;
import com.ayman.bankapp.bankingapplication.entities.Account;
import com.ayman.bankapp.bankingapplication.entities.User;

import java.util.List;

public interface UserService {
    UserResponse registerUser(UserRegistrationRequest userRegistrationRequest);
    UserLoginResponse loginUser(UserLoginRequest userLoginRequest);
    List<Account> getUserAccounts(String userId);
    User getUserByEmail(String email);
    void removeUser(String userId);
    List<User> getAllUsers();
    void saveRefreshToken(String email, String refreshToken);
}
