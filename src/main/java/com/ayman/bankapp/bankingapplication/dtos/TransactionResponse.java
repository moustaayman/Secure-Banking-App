package com.ayman.bankapp.bankingapplication.dtos;

import com.ayman.bankapp.bankingapplication.entities.Transaction;
import lombok.Builder;

@Builder
public record TransactionResponse(String responseMessage, Transaction transactionInfo) {
}
