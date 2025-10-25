package com.example.mortgageservice.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Interest rate details
 */
@Schema(name = "InterestRate", description = "Interest rate details")
public record MortgageRate (
        @Schema(name = "rate", example = "5.75", description = "Percentage value (0-100)")
        Double rate,
        @Schema(name = "tenure", example = "30", description = "Tenure in years")
        Integer tenure,
        @Schema(name = "interestType", description = "Type of interest")
        InterestTypeEnum interestType
) {}

