package com.ayman.bankapp.bankingapplication.dtos;

import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record TransferRequest(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
}
