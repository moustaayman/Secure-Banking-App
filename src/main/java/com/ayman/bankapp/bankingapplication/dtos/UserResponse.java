package com.ayman.bankapp.bankingapplication.dtos;

import com.ayman.bankapp.bankingapplication.enums.Role;
import lombok.Builder;

import java.util.Set;

@Builder
public record UserResponse(String firstName, String lastName, String email, Set<Role> roles) {
}
