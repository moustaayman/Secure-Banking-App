package com.ayman.bankapp.bankingapplication.repositories;

import com.ayman.bankapp.bankingapplication.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(String accountNumber);
    Account findByAccountNumber(String accountNumber);
}
