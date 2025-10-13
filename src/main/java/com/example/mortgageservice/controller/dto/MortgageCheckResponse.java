package com.example.mortgageservice.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;

import java.util.Objects;

/**
 * Result of mortgage eligibility check
 */

@Schema(name = "MortgageCheckResponse", description = "Result of mortgage eligibility check")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-13T17:12:32.542554800+05:30[Asia/Calcutta]", comments = "Generator version: 7.7.0")
public class MortgageCheckResponse {

  private Boolean eligible;

  private Double mortgageAmountMonthly;

  public MortgageCheckResponse eligible(Boolean eligible) {
    this.eligible = eligible;
    return this;
  }

  /**
   * Eligibility decision
   * @return eligible
   */
  
  @Schema(name = "eligible", example = "true", description = "Eligibility decision", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eligible")
  public Boolean getEligible() {
    return eligible;
  }

  public void setEligible(Boolean eligible) {
    this.eligible = eligible;
  }

  public MortgageCheckResponse mortgageAmountMonthly(Double mortgageAmountMonthly) {
    this.mortgageAmountMonthly = mortgageAmountMonthly;
    return this;
  }

  /**
   * Monthly mortgage amount
   * @return mortgageAmountMonthly
   */
  
  @Schema(name = "mortgageAmountMonthly", example = "1500", description = "Monthly mortgage amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mortgageAmountMonthly")
  public Double getMortgageAmountMonthly() {
    return mortgageAmountMonthly;
  }

  public void setMortgageAmountMonthly(Double mortgageAmountMonthly) {
    this.mortgageAmountMonthly = mortgageAmountMonthly;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MortgageCheckResponse mortgageCheckResponse = (MortgageCheckResponse) o;
    return Objects.equals(this.eligible, mortgageCheckResponse.eligible) &&
        Objects.equals(this.mortgageAmountMonthly, mortgageCheckResponse.mortgageAmountMonthly);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eligible, mortgageAmountMonthly);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MortgageCheckResponse {\n");
    sb.append("    eligible: ").append(toIndentedString(eligible)).append("\n");
    sb.append("    mortgageAmountMonthly: ").append(toIndentedString(mortgageAmountMonthly)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

