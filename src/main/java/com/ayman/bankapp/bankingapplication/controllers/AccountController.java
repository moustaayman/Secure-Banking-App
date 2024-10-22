package com.ayman.bankapp.bankingapplication.controllers;

import com.ayman.bankapp.bankingapplication.dtos.AccountRequest;
import com.ayman.bankapp.bankingapplication.dtos.AccountResponse;
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
    public ResponseEntity<AccountResponse> createUser(@RequestBody AccountRequest accountRequest) {
        return new ResponseEntity<>(accountService.createAccount(accountRequest.email(), accountRequest.accountName()), HttpStatus.CREATED);
    }
}