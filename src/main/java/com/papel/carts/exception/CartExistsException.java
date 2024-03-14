package com.papel.carts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CartExistsException extends RuntimeException{

    public CartExistsException(String message) {
        super(message);
    }
    public CartExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
