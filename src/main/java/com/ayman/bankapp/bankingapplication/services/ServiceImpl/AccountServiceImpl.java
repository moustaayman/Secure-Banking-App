package com.ayman.bankapp.bankingapplication.services.ServiceImpl;

import com.ayman.bankapp.bankingapplication.dtos.AccountBalanceResponse;
import com.ayman.bankapp.bankingapplication.dtos.AccountResponse;
import com.ayman.bankapp.bankingapplication.dtos.EmailDetails;
import com.ayman.bankapp.bankingapplication.entities.Account;
import com.ayman.bankapp.bankingapplication.entities.User;
import com.ayman.bankapp.bankingapplication.enums.AccountStatus;
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
import java.util.List;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private EmailService emailService;


    private final String htmlAccountCreation = "<html><body><h1>Account Created</h1><p>Dear %s, your account has been created successfully!</p></body></html>";
    private final String htmlAccountDeletion = "<html><body><h1>Account Closed</h1><p>Dear %s, your account has been closed successfully!</p></body></html>";
    @Override
    @Transactional
    public AccountResponse createAccount(String userID, String accountName) {
        if (!userRepository.existsById(userID)) {
            throw CustomException.notFound("No user found with the specified id");
        }
        User user = userRepository.findUserById(userID);
        Account newAccount = Account.builder()
                .accountName(accountName)
                .accountNumber(generateUniqueAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .status(AccountStatus.ACTIVE)
                .user(user)
                .build();

        accountRepository.save(newAccount);

        //add the acc to the user's acc list
        user.getAccounts().add(newAccount);
        userRepository.save(user);

        String htmlBody = String.format(htmlAccountCreation, user.getFirstName());
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

    @Override
    public AccountBalanceResponse getAccountBalance(String accountNumber) {
        // checking if the account exists
        if (!accountRepository.existsByAccountNumber(accountNumber)) {
            throw CustomException.notFound("No account found with the specified account number");
        }
        return AccountBalanceResponse.builder()
                .balance(accountRepository.findByAccountNumber(accountNumber).getAccountBalance())
                .build();
    }

    @Override
    @Transactional
    public AccountResponse closeAccount(String accountNumber) {
        // checking if the account exists
        if (!accountRepository.existsByAccountNumber(accountNumber)) {
            throw CustomException.notFound("No account found with the specified account number");
        }

        Account account = accountRepository.findByAccountNumber(accountNumber);

        // checking if the account has a positive balance
        if (account.getAccountBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw CustomException.badRequest("Cannot close account with remaining balance, please withdraw the balance first");
        }

        // removing the account from the user
        User user = account.getUser();
//        logger.info("the user before deleting its account: {}", user.getAccounts());
        user.getAccounts().remove(account);

        account.setUser(null);

        userRepository.save(user);
//        logger.info("the user after deleting its account: {}", user.getAccounts());

        // no need for calling the delete method from the acc repo because the accounts list is orphan deleted

        String htmlBody = String.format(htmlAccountDeletion, user.getFirstName());
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("Account Closed Successfully!")
                .body(htmlBody)
                .build();
        emailService.sendSimpleMail(emailDetails);

//        logger.info("after sending the email");

        return AccountResponse.builder()
                .responseMessage("Account closed successfully")
                .accountInfo(Account.builder()
                        .accountNumber(account.getAccountNumber())
                        .accountName(account.getAccountName())
                        .accountBalance(account.getAccountBalance())
                        .build())
                .build();
    }

    private String generateUniqueAccountNumber() {
        long uniqueNumber = System.currentTimeMillis();
        return String.format("%010d", uniqueNumber);
    }
}
