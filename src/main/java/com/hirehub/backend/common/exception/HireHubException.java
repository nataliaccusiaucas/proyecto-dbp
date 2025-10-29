package com.hirehub.backend.common.exception;

public class HireHubException extends RuntimeException {
    public HireHubException(String message) {
        super(message);
    }

    public HireHubException(String message, Throwable cause) {
        super(message, cause);
    }
}