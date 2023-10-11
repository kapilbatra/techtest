package com.db.dataplatform.techtest.server.exception;

public class ChecksumNotMatchingException extends RuntimeException {

    public ChecksumNotMatchingException(final String message) {
        super(message);
    }

    public ChecksumNotMatchingException(final Throwable cause) {
        super(cause);
    }

    ChecksumNotMatchingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
