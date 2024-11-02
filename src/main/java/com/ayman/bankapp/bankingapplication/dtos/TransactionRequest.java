package com.ayman.bankapp.bankingapplication.dtos;

import java.math.BigDecimal;

public record TransactionRequest(String accountNumber, BigDecimal amount) {
}
