package com.example.mortgageservice.controller.exceptions;

public class BadRequestException extends RuntimeException{
    private String message;

    public BadRequestException(String message) {
        this.message = message;
    }
}
