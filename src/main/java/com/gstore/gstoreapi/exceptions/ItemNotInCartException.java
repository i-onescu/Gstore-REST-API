package com.gstore.gstoreapi.exceptions;

public class ItemNotInCartException extends RuntimeException {

    public ItemNotInCartException() {
    }

    public ItemNotInCartException(String message) {
        super(message);
    }

    public ItemNotInCartException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemNotInCartException(Throwable cause) {
        super(cause);
    }

    public ItemNotInCartException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
