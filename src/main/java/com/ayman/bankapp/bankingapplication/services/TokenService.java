package com.ayman.bankapp.bankingapplication.services;

public interface TokenService {
    String refreshAccessToken(String refreshToken);
}
