package com.renato.redminetimer.exception;

public class RedmineIntegrationException extends RuntimeException {

    public RedmineIntegrationException(String message) {
        super(message);
    }

    public RedmineIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
