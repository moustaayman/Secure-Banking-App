package com.ayman.bankapp.bankingapplication.dtos;

import com.ayman.bankapp.bankingapplication.enums.TransactionType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record TransactionHistoryResponse(BigDecimal amount, LocalDateTime transactionDate, TransactionType type, String recipientAccountNumber) {
}
