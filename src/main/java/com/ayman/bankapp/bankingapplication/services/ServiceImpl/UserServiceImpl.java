package com.ayman.bankapp.bankingapplication.services.ServiceImpl;

import com.ayman.bankapp.bankingapplication.dtos.UserRegistrationRequest;
import com.ayman.bankapp.bankingapplication.dtos.UserResponse;
import com.ayman.bankapp.bankingapplication.entities.User;
import com.ayman.bankapp.bankingapplication.enums.Role;
import com.ayman.bankapp.bankingapplication.exceptions.CustomException;
import com.ayman.bankapp.bankingapplication.repositories.UserRepository;
import com.ayman.bankapp.bankingapplication.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    @Override
    @Transactional
    public UserResponse registerUser(UserRegistrationRequest userRegistrationRequest) {
        //checking if the user already has an account
        if(userRepository.existsByEmail(userRegistrationRequest.email())) {
            throw new CustomException.BadRequestException("Email is already in use");
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
                .roles(Set.of(Role.USER))
                .registrationDate(LocalDateTime.now())
                .build();
        userRepository.save(user);
        return UserResponse.builder()
                .responseMessage("SUCCESS")
                .userInfo(User.builder()
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .roles(user.getRoles())
                        .build())
                .build();
    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }
}
