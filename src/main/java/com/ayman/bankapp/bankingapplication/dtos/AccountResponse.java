package com.ayman.bankapp.bankingapplication.dtos;

import com.ayman.bankapp.bankingapplication.entities.Account;
import lombok.Builder;
@Builder
public record AccountResponse(String responseMessage, Account accountInfo) {}