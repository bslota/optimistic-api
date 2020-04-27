package com.bslota.optimisticapi.holding.infrastructure.rest;

/**
 * @author bslota on 26/04/2020
 */
class ErrorMessage {
    private final String message;

    private ErrorMessage(String message) {
        this.message = message;
    }

    static ErrorMessage from(String message) {
        return new ErrorMessage(message);
    }
}
