package com.example.mortgageservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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


