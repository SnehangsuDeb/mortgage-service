package com.example.mortgageservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * Mortgage check request payload
 */

@Schema(name = "MortgageCheckRequest", description = "Mortgage check request payload")
public record MortgageCheckRequest(
        @Schema(name = "income", description = "Monthly income")
        @NotNull @Positive BigDecimal income,
        @Schema(name = "maturityPeriod", description = "Maturity period in years")
        @NotNull @Positive Integer maturityPeriod,
        @Schema(name = "loanValue", description = "Loan amount")
        @NotNull @Positive BigDecimal loanValue,
        @Schema(name = "homeValue", description = "Home value")
        @NotNull @Positive BigDecimal homeValue
) {}
