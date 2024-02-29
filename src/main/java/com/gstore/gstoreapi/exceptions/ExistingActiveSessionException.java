package com.gstore.gstoreapi.exceptions;

public class ExistingActiveSessionException extends RuntimeException {

    public ExistingActiveSessionException() {
    }

    public ExistingActiveSessionException(String message) {
        super(message);
    }

    public ExistingActiveSessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistingActiveSessionException(Throwable cause) {
        super(cause);
    }

    public ExistingActiveSessionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
