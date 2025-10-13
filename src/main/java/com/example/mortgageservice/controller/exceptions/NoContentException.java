package com.example.mortgageservice.controller.exceptions;

import lombok.Data;

@Data
public class NoContentException extends RuntimeException{
    private String message;
    public NoContentException(String message) {
        this.message = message;
    }
}
