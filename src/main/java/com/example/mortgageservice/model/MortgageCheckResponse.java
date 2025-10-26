package com.example.mortgageservice.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Result of mortgage eligibility check
 */
@Schema(name = "MortgageCheckResponse", description = "Result of mortgage eligibility check")
public record MortgageCheckResponse (
        @Schema(name = "eligible", description = "Mortgage eligibility")
        Boolean eligible,
        @Schema(name = "mortgageAmountMonthly", description = "Monthly mortgage amount")
        Double mortgageAmountMonthly
        ){}
