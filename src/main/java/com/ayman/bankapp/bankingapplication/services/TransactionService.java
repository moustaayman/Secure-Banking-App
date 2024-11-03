package com.ayman.bankapp.bankingapplication.services;

import com.ayman.bankapp.bankingapplication.dtos.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    AccountResponse deposit(String accountNumber, BigDecimal amount);
    AccountResponse withdrawal(String accountNumber, BigDecimal amount);
    TransferResponse transfer(TransferRequest transferRequest);
    List<TransactionHistoryResponse> getTransactionHistory(String accountNumber);
    StatementResponse getTransactionsByDateRange(String accountNumber, LocalDate startDate, LocalDate endDate);
}
