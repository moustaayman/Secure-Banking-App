package com.ayman.bankapp.bankingapplication.dtos;

import com.ayman.bankapp.bankingapplication.entities.User;
import lombok.Builder;
@Builder
public record UserResponse(String responseMessage, User userInfo) {
}
