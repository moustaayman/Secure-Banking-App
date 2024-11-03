package com.ayman.bankapp.bankingapplication.dtos;

import lombok.Builder;

import java.util.List;
@Builder
public record StatementResponse(String accountNumber, List<TransactionHistoryResponse> transactions) {
}
