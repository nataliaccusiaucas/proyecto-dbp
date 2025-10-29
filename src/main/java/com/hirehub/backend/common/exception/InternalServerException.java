package com.hirehub.backend.common.exception;

public class InternalServerException extends HireHubException {
    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }
}