package com.ayman.bankapp.bankingapplication.controllers;

import com.ayman.bankapp.bankingapplication.dtos.TransactionHistoryResponse;
import com.ayman.bankapp.bankingapplication.dtos.TransferRequest;
import com.ayman.bankapp.bankingapplication.dtos.TransferResponse;
import com.ayman.bankapp.bankingapplication.services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@AllArgsConstructor
public class TransactionController {
    private TransactionService transactionService;

    @PostMapping(path = "/transfer")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest transferRequest) {
        return new ResponseEntity<>(transactionService.transfer(transferRequest), HttpStatus.OK);
    }
    @GetMapping(path = "/history/{accountNumber}")
    public ResponseEntity<List<TransactionHistoryResponse>> getTransactionHistory(@PathVariable String accountNumber) {
        return new ResponseEntity<>(transactionService.getTransactionHistory(accountNumber), HttpStatus.OK);
    }
}
