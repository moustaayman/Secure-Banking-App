package com.ayman.bankapp.bankingapplication.services;

import java.time.LocalDate;

public interface BankStatementService {
    byte[] generateBankStatement(String accountNumber, LocalDate startDate, LocalDate endDate);
}
