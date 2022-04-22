package com.strawbwine.tasks.backend;

public class NegativeDurationException extends IllegalArgumentException {
    public NegativeDurationException(String message) {
        super(message);
    }
}
