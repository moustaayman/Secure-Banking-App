package com.ayman.bankapp.bankingapplication.dtos;

import lombok.Builder;

@Builder
public record BankResponse(String responseCode, String responseMessage, AccountInfo accountInfo) {}
