package com.ayman.bankapp.bankingapplication.dtos;

import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record AccountInfo (String accountName, String accountNumber, BigDecimal accountBalance) {}