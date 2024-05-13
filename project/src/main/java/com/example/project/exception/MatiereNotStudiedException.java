package com.example.project.exception;

public class MatiereNotStudiedException extends IllegalArgumentException {
    public MatiereNotStudiedException(String message) {
        super(message);
    }
}