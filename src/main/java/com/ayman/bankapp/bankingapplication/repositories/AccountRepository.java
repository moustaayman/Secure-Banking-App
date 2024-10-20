package com.ayman.bankapp.bankingapplication.repositories;

import com.ayman.bankapp.bankingapplication.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(String accountNumber);
}
