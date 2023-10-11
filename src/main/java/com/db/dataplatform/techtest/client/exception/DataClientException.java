package com.db.dataplatform.techtest.client.exception;

public class DataClientException extends RuntimeException {

    public DataClientException(final String message) {
        super(message);
    }

    public DataClientException(final Throwable cause) {
        super(cause);
    }

    DataClientException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
