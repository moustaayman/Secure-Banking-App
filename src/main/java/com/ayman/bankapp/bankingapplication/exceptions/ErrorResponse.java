package com.ayman.bankapp.bankingapplication.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ErrorResponse {
    private String errorCode;
    private String message;
}
