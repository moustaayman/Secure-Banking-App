package com.ayman.bankapp.bankingapplication.dtos;

import com.ayman.bankapp.bankingapplication.enums.Gender;

public record UserRegistrationRequest(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String address,
        String city,
        Gender gender,
        String password
) {}
