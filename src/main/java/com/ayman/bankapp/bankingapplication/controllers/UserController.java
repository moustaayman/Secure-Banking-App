package com.ayman.bankapp.bankingapplication.controllers;

import com.ayman.bankapp.bankingapplication.config.JwtUtil;
import com.ayman.bankapp.bankingapplication.dtos.*;
import com.ayman.bankapp.bankingapplication.entities.Account;
import com.ayman.bankapp.bankingapplication.entities.User;
import com.ayman.bankapp.bankingapplication.enums.Role;
import com.ayman.bankapp.bankingapplication.exceptions.CustomException;
import com.ayman.bankapp.bankingapplication.services.TokenService;
import com.ayman.bankapp.bankingapplication.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private final TokenService tokenService;
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
    @PostMapping(path = "/register")
    public ResponseEntity<UserResponse> registerUser(@Validated @RequestBody UserRegistrationRequest userRegistrationRequest) {
        return new ResponseEntity<>(userService.registerUser(userRegistrationRequest), HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        return new ResponseEntity<>(userService.loginUser(userLoginRequest), HttpStatus.OK);
    }

    @GetMapping("/{userId}/accounts")
    public ResponseEntity<List<Account>> getUserAccounts(@PathVariable String userId) {
        return new ResponseEntity<>(userService.getUserAccounts(userId), HttpStatus.OK);
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest refreshToken) {
        return new ResponseEntity<>(new RefreshTokenResponse(tokenService.refreshAccessToken(refreshToken.refreshToken())), HttpStatus.OK);
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeUser(@PathVariable String userId) {
        userService.removeUser(userId);
        return ResponseEntity.noContent().build();
    }
}
