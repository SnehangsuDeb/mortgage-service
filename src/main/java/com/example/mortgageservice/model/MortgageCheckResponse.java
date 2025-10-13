package com.example.mortgageservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import lombok.Data;

/**
 * Result of mortgage eligibility check
 */

@Data
@Schema(name = "MortgageCheckResponse", description = "Result of mortgage eligibility check")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-13T17:12:32.542554800+05:30[Asia/Calcutta]", comments = "Generator version: 7.7.0")
public class MortgageCheckResponse {

  private Boolean eligible;

  private Double mortgageAmountMonthly;

    public MortgageCheckResponse (Boolean eligible, Double mortgageAmountMonthly) {
      this.eligible = eligible;
      this.mortgageAmountMonthly = mortgageAmountMonthly;
  }
}

