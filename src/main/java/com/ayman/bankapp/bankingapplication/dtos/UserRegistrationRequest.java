package com.ayman.bankapp.bankingapplication.dtos;

import com.ayman.bankapp.bankingapplication.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRegistrationRequest(
        @NotBlank(message = "First name is required")
        String firstName,
        @NotBlank(message = "Last name is required")
        String lastName,
        @Email(message = "Email should be valid")
        @NotBlank(message = "Email name is required")
        String email,
        @NotBlank(message = "Phone number name is required")
        String phoneNumber,
        @NotBlank(message = "Address is required")
        String address,
        @NotBlank(message = "City is required")
        String city,
        @NotBlank(message = "Gender is required")
        @Pattern(regexp = "MALE|FEMALE", message = "Gender must be one of MALE or FEMALE")
        Gender gender,
        @NotBlank(message = "Password is required")
        String password
) {}
