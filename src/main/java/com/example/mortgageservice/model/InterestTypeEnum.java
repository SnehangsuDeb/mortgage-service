package com.example.mortgageservice.model;

import java.util.Locale;

public enum InterestTypeEnum {
    FIXED,
    VARIABLE;

    public static InterestTypeEnum fromNullable(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
        String normalized = value.trim().toUpperCase(Locale.ROOT);
        switch (normalized) {
            case "FIXED":
                return FIXED;
            case "VARIABLE":
                return VARIABLE;
            default:
                throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }
}
