package com.ayman.bankapp.bankingapplication.dtos;

import lombok.Builder;

@Builder
public record UserLoginResponse(String accessToken, String refreshToken) {
}
