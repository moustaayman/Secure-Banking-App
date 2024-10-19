package com.ayman.bankapp.bankingapplication.dtos;

import com.ayman.bankapp.bankingapplication.enums.UserStatus;

public record UserRequest(
        String firstName,
        String lastName,
        String otherName,
        String gender,
        String address,
        String city,
        String email,
        String phoneNumber,
        String alternativePhoneNumber
) {}
