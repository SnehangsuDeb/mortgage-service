package com.example.mortgageservice.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Wrapper object containing interest rates
 */

@Schema(name = "InterestRatesResponse", description = "Wrapper object containing interest rates")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-13T17:12:32.542554800+05:30[Asia/Calcutta]", comments = "Generator version: 7.7.0")
public class InterestRatesResponse {

  @Valid
  private List<@Valid InterestRate> interestRates = new ArrayList<>();

  public InterestRatesResponse interestRates(List<@Valid InterestRate> interestRates) {
    this.interestRates = interestRates;
    return this;
  }

  public InterestRatesResponse addInterestRatesItem(InterestRate interestRatesItem) {
    if (this.interestRates == null) {
      this.interestRates = new ArrayList<>();
    }
    this.interestRates.add(interestRatesItem);
    return this;
  }

  /**
   * Get interestRates
   * @return interestRates
   */
  @Valid 
  @Schema(name = "interestRates", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("interestRates")
  public List<@Valid InterestRate> getInterestRates() {
    return interestRates;
  }

  public void setInterestRates(List<@Valid InterestRate> interestRates) {
    this.interestRates = interestRates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InterestRatesResponse interestRatesResponse = (InterestRatesResponse) o;
    return Objects.equals(this.interestRates, interestRatesResponse.interestRates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(interestRates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InterestRatesResponse {\n");
    sb.append("    interestRates: ").append(toIndentedString(interestRates)).append("\n");
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

