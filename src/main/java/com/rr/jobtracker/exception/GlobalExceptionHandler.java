package com.rr.jobtracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

//duplicate email exception handle
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExists(EmailAlreadyExistsException ex){

        Map<String, String> error = Map.of("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

//    validation error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(
                        error.getField(),
                        error.getDefaultMessage()
                )
        );
        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentials(InvalidCredentialException ex){

        Map<String, String> error = Map.of("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(
            RuntimeException ex) {

        Map<String, String> error =
                Map.of("message", ex.getMessage());

        if ("You have already applied for this job"
                .equals(ex.getMessage())) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(error);
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

}
