package com.example.mortgageservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Mortgage check request payload
 */

@Schema(name = "MortgageCheckRequest", description = "Mortgage check request payload")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-13T17:12:32.542554800+05:30[Asia/Calcutta]", comments = "Generator version: 7.7.0")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MortgageCheckRequest {

  @NotNull @Positive private BigDecimal income;
  @NotNull @Positive private Integer maturityPeriod;
  @NotNull @Positive private BigDecimal loanValue;
  @NotNull @Positive private BigDecimal homeValue;
}

