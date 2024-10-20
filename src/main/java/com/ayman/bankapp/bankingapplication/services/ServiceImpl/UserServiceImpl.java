package com.ayman.bankapp.bankingapplication.services.ServiceImpl;

import com.ayman.bankapp.bankingapplication.dtos.UserRegistrationRequest;
import com.ayman.bankapp.bankingapplication.dtos.UserResponse;
import com.ayman.bankapp.bankingapplication.entities.User;
import com.ayman.bankapp.bankingapplication.enums.Role;
import com.ayman.bankapp.bankingapplication.exceptions.CustomException;
import com.ayman.bankapp.bankingapplication.repositories.UserRepository;
import com.ayman.bankapp.bankingapplication.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    @Override
    public UserResponse registerUser(UserRegistrationRequest userRegistrationRequest) {
        //
        log.info("Registering user with email: {}", userRegistrationRequest.email());
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
                .build();
        userRepository.save(user);
        return UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }
}
