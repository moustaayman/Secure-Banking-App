package com.ayman.bankapp.bankingapplication.controllers;

import com.ayman.bankapp.bankingapplication.dtos.*;
import com.ayman.bankapp.bankingapplication.services.AccountService;
import com.ayman.bankapp.bankingapplication.services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
@RestController
@RequestMapping("/api/v1/accounts")
@AllArgsConstructor
public class AccountController {
    private AccountService accountService;
    private TransactionService transactionService;

    @PostMapping(path = "/create")
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest accountRequest) {
        return new ResponseEntity<>(accountService.createAccount(accountRequest.email(), accountRequest.accountName()), HttpStatus.CREATED);
    }
    @GetMapping(path = "/balance/{accountNumber}")
    public ResponseEntity<AccountBalanceResponse> createAccount(@PathVariable String accountNumber) {
        return new ResponseEntity<>(accountService.getAccountBalance(accountNumber), HttpStatus.OK);
    }
    @PostMapping(path = "/deposit/{accountNumber}")
    public ResponseEntity<AccountResponse> deposit(@PathVariable String accountNumber, @RequestBody TransactionRequest transactionRequest) {
        return new ResponseEntity<>(transactionService.deposit(accountNumber, transactionRequest.amount()), HttpStatus.OK);
    }

    @PostMapping(path = "/withdrawal/{accountNumber}")
    public ResponseEntity<AccountResponse> withdrawal(@PathVariable String accountNumber, @RequestBody TransactionRequest transactionRequest) {
        return new ResponseEntity<>(transactionService.withdrawal(accountNumber, transactionRequest.amount()), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{accountNumber}")
    public ResponseEntity<AccountResponse> deleteAccount(@PathVariable String accountNumber) {
        return new ResponseEntity<>(accountService.closeAccount(accountNumber), HttpStatus.OK);
    }
}