package com.gstore.gstoreapi.exceptions;

public class InvalidSessionIdException extends RuntimeException {

    public InvalidSessionIdException() {
    }

    public InvalidSessionIdException(String message) {
        super(message);
    }

    public InvalidSessionIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSessionIdException(Throwable cause) {
        super(cause);
    }

    public InvalidSessionIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
