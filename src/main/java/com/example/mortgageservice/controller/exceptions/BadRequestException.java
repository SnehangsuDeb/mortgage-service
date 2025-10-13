package com.example.mortgageservice.controller.exceptions;

import lombok.Data;

@Data
public class BadRequestException extends RuntimeException{
    private String message;

    public BadRequestException(String message) {
        this.message = message;
    }
}
