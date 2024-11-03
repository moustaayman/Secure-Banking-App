package com.ayman.bankapp.bankingapplication.repositories;

import com.ayman.bankapp.bankingapplication.entities.Account;
import com.ayman.bankapp.bankingapplication.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
     List<Transaction> findByFromAccount(Account account);
     List<Transaction> findByToAccount(Account account);
     List<Transaction> findByFromAccountAndTransactionDateBetween(Account account, LocalDateTime startDate, LocalDateTime endDate);
     List<Transaction> findByToAccountAndTransactionDateBetween(Account account, LocalDateTime startDate, LocalDateTime endDate);
}
