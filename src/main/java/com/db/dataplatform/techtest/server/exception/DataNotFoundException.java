package com.db.dataplatform.techtest.server.exception;

public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException(final String message) {
        super(message);
    }

    public DataNotFoundException(final Throwable cause) {
        super(cause);
    }

    DataNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
