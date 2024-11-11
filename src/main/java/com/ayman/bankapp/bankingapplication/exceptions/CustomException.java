package com.ayman.bankapp.bankingapplication.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
public class CustomException extends RuntimeException{
    private final HttpStatus status;

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public static CustomException notFound(String message) {
        return new CustomException(message, HttpStatus.NOT_FOUND);
    }

    public static CustomException badRequest(String message) {
        return new CustomException(message, HttpStatus.BAD_REQUEST);
    }

    public static CustomException unauthenticated(String message) {
        return new CustomException(message, HttpStatus.UNAUTHORIZED);
    }

    public static CustomException unauthorized(String message) {
        return new CustomException(message, HttpStatus.FORBIDDEN);
    }
}
