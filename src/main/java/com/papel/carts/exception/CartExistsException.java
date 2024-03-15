package com.papel.carts.exception;

public class CartExistsException extends RuntimeException{

    public CartExistsException(String message) {
        super(message);
    }
    public CartExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
