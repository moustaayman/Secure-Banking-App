package com.ayman.bankapp.bankingapplication.controllers;

import com.ayman.bankapp.bankingapplication.dtos.BankResponse;
import com.ayman.bankapp.bankingapplication.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
@AllArgsConstructor
public class AccountController {
    private AccountService accountService;

    @PostMapping(path = "/create")
    public ResponseEntity<BankResponse> createUser(@RequestBody String email, String accountName) {
        return new ResponseEntity<>(accountService.createAccount(email, accountName), HttpStatus.CREATED);
    }
}