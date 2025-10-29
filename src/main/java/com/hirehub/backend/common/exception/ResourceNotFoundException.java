package com.hirehub.backend.common.exception;

public class ResourceNotFoundException extends HireHubException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}