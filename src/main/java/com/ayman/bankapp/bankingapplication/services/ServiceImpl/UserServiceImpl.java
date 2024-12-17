package com.ayman.bankapp.bankingapplication.services.ServiceImpl;

import com.ayman.bankapp.bankingapplication.config.JwtUtil;
import com.ayman.bankapp.bankingapplication.dtos.UserLoginRequest;
import com.ayman.bankapp.bankingapplication.dtos.UserLoginResponse;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private final AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();

        if(users.isEmpty()) {
            throw CustomException.notFound("No users found");
        }

        return users.stream()
                .map(user -> User.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .build())
                .toList();
    }

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
                .password(passwordEncoder.encode(userRegistrationRequest.password()))
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
                .responseMessage("User registered successfully")
                .userInfo(user)
                .build();
    }

    @Override
    public UserLoginResponse loginUser(UserLoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        } catch (AuthenticationException e) {
            throw CustomException.unauthorized("Invalid credentials");
        }
        if (!authentication.isAuthenticated()) {
            throw CustomException.unauthorized("Invalid credentials");
        }
        //getting the user from the db
        User user = userRepository.findUserByEmail(loginRequest.email())
                .orElseThrow(() -> CustomException.notFound("Invalid credentials"));

        //extracting the user's roles and converting them into a single space separated String
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority) // Extracts roles like "ROLE_CUSTOMER", "ROLE_ADMIN"
                .flatMap(role -> Arrays.stream(role.replaceFirst("ROLE_", "").split("[\\[\\],]"))) // Remove "ROLE_" and split if brackets are present
                .map(String::trim) // Remove any extra spaces
                .filter(role -> !role.isEmpty()) // Remove empty strings from split results
                .collect(Collectors.joining(" ")); // Join roles with a space

        //generating the access token and refresh token
        Instant instant = Instant.now();
//        System.out.println(user.getEmail() + " " + user.getId() + " " + scope + " " + instant + " " + 60);
        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getId(), scope, instant, 60);
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail(), 7);

        saveRefreshToken(user.getEmail(), refreshToken);

        return UserLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void saveRefreshToken(String email, String refreshToken) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> CustomException.notFound("User not found"));
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
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

    @Override
    public User getUserByEmail(String email) {
            return userRepository.findUserByEmail((email)).orElseThrow(() -> CustomException.notFound("User not found"));
    }
    @Override
    @Transactional
    public void removeUser(String userId) {
        User user = userRepository.findUserById(userId);

        if(!userRepository.existsById(userId)) {
            throw CustomException.badRequest("No user found with the specified id");
        }
        user.setStatus(UserStatus.DELETED);
        userRepository.save(user);
//        userRepository.delete(user);
    }
}
