package com.example.mortgageservice.model;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;

/**
 * Wrapper object containing mortgage rates
 */

@Schema(name = "MortgageRatesResponse", description = "Wrapper object containing mortgage rates")
public record MortgageRatesResponse(
        @Valid
        @ArraySchema(minItems = 0,
                schema = @Schema(implementation = MortgageRate.class,
                        title = "List of interestRates")
        )
        List<MortgageRate> mortgageRates
){}
