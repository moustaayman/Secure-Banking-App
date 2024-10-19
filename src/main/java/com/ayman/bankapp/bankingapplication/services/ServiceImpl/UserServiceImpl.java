package com.ayman.bankapp.bankingapplication.services.ServiceImpl;

import com.ayman.bankapp.bankingapplication.dtos.BankResponse;
import com.ayman.bankapp.bankingapplication.dtos.UserRequest;
import com.ayman.bankapp.bankingapplication.entities.User;
import com.ayman.bankapp.bankingapplication.enums.UserStatus;
import com.ayman.bankapp.bankingapplication.repositories.UserRepository;
import com.ayman.bankapp.bankingapplication.services.UserService;
import com.ayman.bankapp.bankingapplication.utils.AccountUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    @Override
    public BankResponse createUser(UserRequest userRequest) {
        //checking if the user already has an account
        if(userRepository.existsByEmail(userRequest.email())) {
            return BankResponse.builder()
                    .responseCode("ERROR")
                    .responseMessage("User already exists")
                    .build();
        }
        //creating a user
        User newUser = User.builder()
                .firstName(userRequest.firstName())
                .lastName(userRequest.lastName())
                .otherName(userRequest.otherName())
                .gender(userRequest.gender())
                .address(userRequest.address())
                .city(userRequest.city())
                .email(userRequest.email())
                .phoneNumber(userRequest.phoneNumber())
                .alternativePhoneNumber(userRequest.alternativePhoneNumber())
                .status(UserStatus.ACTIVE)
                .build();
        userRepository.save(newUser);
        return BankResponse.builder()
                .responseCode("SUCCESS")
                .responseMessage("Account created successfully")
                .build();
    }
}
