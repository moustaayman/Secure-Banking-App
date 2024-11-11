package com.ayman.bankapp.bankingapplication.services.ServiceImpl;

import com.ayman.bankapp.bankingapplication.dtos.UserRegistrationRequest;
import com.ayman.bankapp.bankingapplication.dtos.UserResponse;
import com.ayman.bankapp.bankingapplication.entities.Account;
import com.ayman.bankapp.bankingapplication.entities.User;
import com.ayman.bankapp.bankingapplication.enums.Role;
import com.ayman.bankapp.bankingapplication.enums.UserStatus;
import com.ayman.bankapp.bankingapplication.exceptions.CustomException;
import com.ayman.bankapp.bankingapplication.repositories.AccountRepository;
import com.ayman.bankapp.bankingapplication.repositories.UserRepository;
import com.ayman.bankapp.bankingapplication.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private AccountRepository accountRepository;
    @Override
    @Transactional
    public UserResponse registerUser(UserRegistrationRequest userRegistrationRequest) {
        //checking if the user already has an account
        if(userRepository.existsByEmail(userRegistrationRequest.email())) {
            throw CustomException.badRequest("Email is already in use");
        }
        //creating a user
        User user = User.builder()
                .firstName(userRegistrationRequest.firstName())
                .lastName(userRegistrationRequest.lastName())
                .email(userRegistrationRequest.email())
                .password(userRegistrationRequest.password())
                .phoneNumber(userRegistrationRequest.phoneNumber())
                .address(userRegistrationRequest.address())
                .city(userRegistrationRequest.city())
                .gender(userRegistrationRequest.gender())
                .roles(Set.of(Role.CUSTOMER))
                .status(UserStatus.ACTIVE)
                .registrationDate(LocalDateTime.now())
                .build();
        userRepository.save(user);
        return UserResponse.builder()
                .responseMessage("SUCCESS")
                .userInfo(user)
                .build();
    }
    @Override
    public List<Account> getUserAccounts(String userId) {
        if(!userRepository.existsById(userId)) {
            throw CustomException.badRequest("No user found with the specified id");
        }

        User user = userRepository.findUserById(userId);

        List<Account> accounts = accountRepository.findByUser(user);

        return accounts.stream().map(account -> Account.builder()
                        .accountNumber(account.getAccountNumber())
                        .accountName(account.getAccountName())
                        .createdAt(account.getCreatedAt())
                        .status(account.getStatus())
                        .accountBalance(account.getAccountBalance())
//                        .user(User.builder()
//                                .firstName(user.getFirstName())
//                                .lastName(user.getLastName())
//                                .roles(user.getRoles())
//                                .phoneNumber(user.getPhoneNumber())
//                                .build())
                        .build()).toList();
    }

}
