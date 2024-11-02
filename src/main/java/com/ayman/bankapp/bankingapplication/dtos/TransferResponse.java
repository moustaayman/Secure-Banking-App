package com.ayman.bankapp.bankingapplication.dtos;

import com.ayman.bankapp.bankingapplication.entities.Transaction;
import com.ayman.bankapp.bankingapplication.enums.TransactionType;
import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record TransferResponse(String responseMessage, Transaction transactionInfo) {
}
