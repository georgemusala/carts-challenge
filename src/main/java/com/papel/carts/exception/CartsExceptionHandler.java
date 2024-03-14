package com.papel.carts.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class CartsExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValid(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage).toList();

        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(CartNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CartExistsException.class)
    public ResponseEntity<?> handleCartExists(CartExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<?> handleLockingFailure(ObjectOptimisticLockingFailureException ex) {
        return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.LOCKED);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}
