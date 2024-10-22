package com.ayman.bankapp.bankingapplication.services.ServiceImpl;

import com.ayman.bankapp.bankingapplication.dtos.AccountResponse;
import com.ayman.bankapp.bankingapplication.dtos.EmailDetails;
import com.ayman.bankapp.bankingapplication.entities.Account;
import com.ayman.bankapp.bankingapplication.entities.User;
import com.ayman.bankapp.bankingapplication.exceptions.CustomException;
import com.ayman.bankapp.bankingapplication.repositories.AccountRepository;
import com.ayman.bankapp.bankingapplication.repositories.UserRepository;
import com.ayman.bankapp.bankingapplication.services.AccountService;
import com.ayman.bankapp.bankingapplication.services.EmailService;
import com.ayman.bankapp.bankingapplication.utils.AccountUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private EmailService emailService;
    private final String html = "<html><body><h1>Account Created</h1><p>Dear %s, your account has been created successfully!</p></body></html>";
    @Override
    public AccountResponse createAccount(String email, String accountName) {
        // Find the user by email
        if (!userRepository.existsByEmail(email)) {
            throw new CustomException.BadRequestException("No user found with the specified email");
        }
        User user = userRepository.findByEmail(email);
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

        // Prepare and send the email
        String htmlBody = String.format(html, user.getFirstName());
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("Account Created Successfully!")
                .body(htmlBody)
                .build();

        emailService.sendSimpleMail(emailDetails);

        return AccountResponse.builder()
                .responseMessage("Account created successfully")
                .accountInfo(Account.builder()
                        .accountName(newAccount.getAccountName())
                        .accountNumber(newAccount.getAccountNumber())
                        .accountBalance(newAccount.getAccountBalance())
                        .user(User.builder()
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .email(user.getEmail())
                                .roles(user.getRoles())
                                .registrationDate(user.getRegistrationDate())
                                .build())
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
