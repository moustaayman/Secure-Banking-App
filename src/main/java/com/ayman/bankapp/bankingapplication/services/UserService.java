package com.ayman.bankapp.bankingapplication.services;

import com.ayman.bankapp.bankingapplication.dtos.UserRegistrationRequest;
import com.ayman.bankapp.bankingapplication.dtos.UserResponse;
import com.ayman.bankapp.bankingapplication.entities.User;

public interface UserService {
    UserResponse registerUser(UserRegistrationRequest userRegistrationRequest);

    User getUserByEmail(String email);
}
