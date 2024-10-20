package com.ayman.bankapp.bankingapplication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(CustomException.NotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomException.BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(CustomException.BadRequestException ex) {
        ErrorResponse errorResponse = new ErrorResponse("BAD_REQUEST", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.UnauthenticatedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthenticatedException(CustomException.UnauthenticatedException ex) {
        ErrorResponse errorResponse = new ErrorResponse("UNAUTHENTICATED", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CustomException.UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(CustomException.UnauthorizedException ex) {
        ErrorResponse errorResponse = new ErrorResponse("UNAUTHORIZED", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    //generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected error occurred");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
