package com.ayman.bankapp.bankingapplication.services.ServiceImpl;

import com.ayman.bankapp.bankingapplication.dtos.AccountInfo;
import com.ayman.bankapp.bankingapplication.dtos.BankResponse;
import com.ayman.bankapp.bankingapplication.entities.Account;
import com.ayman.bankapp.bankingapplication.entities.User;
import com.ayman.bankapp.bankingapplication.repositories.AccountRepository;
import com.ayman.bankapp.bankingapplication.repositories.UserRepository;
import com.ayman.bankapp.bankingapplication.services.AccountService;
import com.ayman.bankapp.bankingapplication.utils.AccountUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private UserRepository userRepository;
    private AccountRepository accountRepository;
    @Override
    public BankResponse createAccount(String email, String accountName) {
        // Find the user by email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return BankResponse.builder()
                    .responseCode("ERROR")
                    .responseMessage("User does not exist")
                    .build();
        }
        // Create a new account for the user
        Account newAccount = Account.builder()
                .accountName(accountName)
                .accountNumber(generateUniqueAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .user(user)
                .build();

        accountRepository.save(newAccount);

        //add the acc to the user's acc list
        user.getAccounts().add(newAccount);
        userRepository.save(user);

        return BankResponse.builder()
                .responseCode("SUCCESS")
                .responseMessage("Account created successfully")
                .accountInfo(AccountInfo.builder()
                        .accountName(newAccount.getAccountName())
                        .accountNumber(newAccount.getAccountNumber())
                        .accountBalance(newAccount.getAccountBalance())
                        .build())
                .build();
    }

    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = AccountUtils.generateAccountNumber();
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }
}
