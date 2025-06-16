package com.betafore.evoting.Exception;


import static com.betafore.evoting.Common.ResponseMessageConstants.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> badCredentialsExceptionHandling(Exception exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.builder()
                .success(false)
                .message(UNAUTHENTICATED)
                .build());
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ApiResponse> accessDeniedExceptionHandling(Exception exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.builder()
                .success(false)
                .message(UNAUTHORIZED)
                .build());
    }

    @ExceptionHandler(io.jsonwebtoken.security.SignatureException.class)
    public ResponseEntity<ApiResponse> signatureExceptionHandling(Exception exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.builder()
                .success(false)
                .message(INVALID_JWT_SIGNATURE)
                .build());
    }

    @ExceptionHandler(io.jsonwebtoken.ExpiredJwtException.class)
    public ResponseEntity<ApiResponse> expiredJwtExceptionHandling(Exception exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.builder()
                .success(false)
                .message(EXPIRED_JWT_SIGNATURE)
                .build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errorMessage.append(message);
        });

        return ResponseEntity.badRequest().body(ApiResponse.builder()
            .success(false)
            .message(errorMessage.toString()).build());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse> handleBusinessException(CustomException ex) {
        return ResponseEntity.badRequest()
            .body(ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .build());
    }
}
