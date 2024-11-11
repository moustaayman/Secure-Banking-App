package com.ayman.bankapp.bankingapplication.services.ServiceImpl;

import com.ayman.bankapp.bankingapplication.dtos.*;
import com.ayman.bankapp.bankingapplication.entities.Account;
import com.ayman.bankapp.bankingapplication.entities.Transaction;
import com.ayman.bankapp.bankingapplication.enums.TransactionType;
import com.ayman.bankapp.bankingapplication.exceptions.CustomException;
import com.ayman.bankapp.bankingapplication.repositories.AccountRepository;
import com.ayman.bankapp.bankingapplication.repositories.TransactionRepository;
import com.ayman.bankapp.bankingapplication.services.TransactionService;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    @Override
    @Transactional
    public AccountResponse deposit(String accountNumber, BigDecimal amount) {
        //finding the account
        if(!accountRepository.existsByAccountNumber(accountNumber)) {
            throw CustomException.badRequest("No account found with the specified account number");
        }
        System.out.println(accountNumber);
        Account account = accountRepository.findByAccountNumber(accountNumber);
        //updating the account balance
        account.setAccountBalance(account.getAccountBalance().add(amount));

        Transaction transaction = Transaction.builder()
                .toAccount(account)
                .amount(amount)
                .type(TransactionType.DEPOSIT)
                .transactionDate(LocalDateTime.now())
                .build();
        transactionRepository.save(transaction);
        //updating the account because its balance has changed
        accountRepository.save(account);
        return AccountResponse.builder()
                .responseMessage("Transaction successful. Deposit of " + amount + " DH was added to account " + accountNumber)
                .accountInfo(Account.builder()
                        .accountNumber(account.getAccountNumber())
                        .accountBalance(account.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    @Transactional
    public AccountResponse withdrawal(String accountNumber, BigDecimal amount) {
        //finding the account
        if(!accountRepository.existsByAccountNumber(accountNumber)) {
            throw CustomException.badRequest("No account found with the specified account number");
        }
        Account account = accountRepository.findByAccountNumber(accountNumber);
        // Checking if there is enough balance to withdraw
        if (account.getAccountBalance().compareTo(amount) < 0) {
            throw CustomException.badRequest("Insufficient balance in account " + accountNumber);
        }
        //updating the account balance
        account.setAccountBalance(account.getAccountBalance().subtract(amount));

        Transaction transaction = Transaction.builder()
                .toAccount(account)
                .amount(amount)
                .type(TransactionType.WITHDRAWAL)
                .transactionDate(LocalDateTime.now())
                .build();
        transactionRepository.save(transaction);
        //updating the account because its balance has changed
        accountRepository.save(account);
        return AccountResponse.builder()
                .responseMessage("Transaction successful. Withdrawal of " + amount + " DH was deducted to account " + accountNumber)
                .accountInfo(Account.builder()
                        .accountNumber(account.getAccountNumber())
                        .accountBalance(account.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    @Transactional
    public TransferResponse transfer(TransferRequest transferRequest) {
        //checking if both accounts exist
        Account fromAccount = accountRepository.findByAccountNumber(transferRequest.fromAccountNumber());
        Account toAccount = accountRepository.findByAccountNumber(transferRequest.toAccountNumber());

        if (fromAccount == null) {
            throw CustomException.badRequest("Source account not found");
        }
        if (toAccount == null) {
            throw CustomException.badRequest("Destination account not found");
        }

        //checking if the source account has enough balance
        if(fromAccount.getAccountBalance().compareTo(transferRequest.amount()) < 0) {
            throw CustomException.badRequest("Insufficient balance in the source account");
        }

        //transfering
        fromAccount.setAccountBalance(fromAccount.getAccountBalance().subtract(transferRequest.amount()));
        toAccount.setAccountBalance(toAccount.getAccountBalance().add(transferRequest.amount()));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction debitTransaction = Transaction.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(transferRequest.amount())
                .type(TransactionType.TRANSFER)
                .transactionDate(LocalDateTime.now())
                .build();

        Transaction creditTransaction = Transaction.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(transferRequest.amount())
                .type(TransactionType.TRANSFER)
                .transactionDate(LocalDateTime.now())
                .build();

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

        return TransferResponse.builder()
                .responseMessage("Transaction successful")
                .transactionInfo(Transaction.builder()
                        .transactionDate(LocalDateTime.now())
                        .type(TransactionType.TRANSFER)
                        .build())
                .build();
    }

    @Override
    public List<TransactionHistoryResponse> getTransactionHistory(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw CustomException.badRequest("Account not found.");
        }

        List<Transaction> outgoingTransactions = transactionRepository.findByFromAccount(account);
        List<Transaction> incomingTransactions = transactionRepository.findByToAccount(account);

        //combining both lists
        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(outgoingTransactions);
        transactions.addAll(incomingTransactions);

        return  transactions.stream().map(transaction ->
            TransactionHistoryResponse.builder()
                    .amount(transaction.getAmount())
                    .transactionDate(transaction.getTransactionDate())
                    .type(transaction.getType())
                    .recipientAccountNumber(transaction.getToAccount() != null ? transaction.getToAccount().getAccountNumber() : null)
                    .build()
        ).collect(Collectors.toList());
    }

    @Override
    public StatementResponse getTransactionsByDateRange(String accountNumber, LocalDate startDate, LocalDate endDate) {
        if(!accountRepository.existsByAccountNumber(accountNumber)) {
            throw CustomException.badRequest("No account found with the specified account number");
        }
        Account account = accountRepository.findByAccountNumber(accountNumber);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<Transaction> outgoingTransactions = transactionRepository.findByFromAccountAndTransactionDateBetween(account, startDateTime, endDateTime);
        List<Transaction> incomingTransactions = transactionRepository.findByToAccountAndTransactionDateBetween(account, startDateTime, endDateTime);

        List<Transaction> allTransactions = new ArrayList<>();
        allTransactions.addAll(outgoingTransactions);
        allTransactions.addAll(incomingTransactions);

        List<TransactionHistoryResponse> transactionHistory = allTransactions.stream().map(transaction ->
                TransactionHistoryResponse.builder()
                        .amount(transaction.getAmount())
                        .transactionDate(transaction.getTransactionDate())
                        .type(transaction.getType())
                        .build()
        ).toList();
        return StatementResponse.builder()
                .accountNumber(accountNumber)
                .transactions(transactionHistory)
                .build();
    }
}
