package com.example.mortgageservice.controller.exceptions;

public class NoContentException extends RuntimeException{
    private String message;
    public NoContentException(String message) {
        this.message = message;
    }
}
