package com.zhansaya.lovelypets.aop;

import com.zhansaya.lovelypets.domain.exceptions.AuthenticationFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<?> handleAuthFailed(AuthenticationFailedException ex) {
        // Log ex.getMessage() if needed
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid phone or password"));
    }

    //TODO handle other exceptions mf
}
