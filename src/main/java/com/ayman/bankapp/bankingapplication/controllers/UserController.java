package com.ayman.bankapp.bankingapplication.controllers;

import com.ayman.bankapp.bankingapplication.dtos.UserRegistrationRequest;
import com.ayman.bankapp.bankingapplication.dtos.UserResponse;
import com.ayman.bankapp.bankingapplication.entities.Account;
import com.ayman.bankapp.bankingapplication.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping(path = "/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        return new ResponseEntity<>(userService.registerUser(userRegistrationRequest), HttpStatus.CREATED);
    }
    @GetMapping("/{userId}/accounts")
    public ResponseEntity<List<Account>> getUserAccounts(@PathVariable String userId) {
        return new ResponseEntity<>(userService.getUserAccounts(userId), HttpStatus.OK);
    }
}
