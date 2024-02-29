package com.gstore.gstoreapi.exceptions;

public class IncorrectLoginDetailsException extends RuntimeException{

    public IncorrectLoginDetailsException() {
    }

    public IncorrectLoginDetailsException(String message) {
        super(message);
    }

    public IncorrectLoginDetailsException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectLoginDetailsException(Throwable cause) {
        super(cause);
    }

    public IncorrectLoginDetailsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
