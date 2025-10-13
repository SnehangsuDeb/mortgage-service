package com.example.mortgageservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Wrapper object containing mortgage rates
 */

@Schema(name = "MortgageRatesResponse", description = "Wrapper object containing mortgage rates")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-13T17:12:32.542554800+05:30[Asia/Calcutta]", comments = "Generator version: 7.7.0")
public class MortgageRatesResponse {

  @Valid
  private List<@Valid MortgageRate> mortgageRates = new ArrayList<>();

  public MortgageRatesResponse mortgageRates(List<@Valid MortgageRate> mortgageRates) {
    this.mortgageRates = mortgageRates;
    return this;
  }

  public MortgageRatesResponse addMortgageRatesItem(MortgageRate mortgageRatesItem) {
    if (this.mortgageRates == null) {
      this.mortgageRates = new ArrayList<>();
    }
    this.mortgageRates.add(mortgageRatesItem);
    return this;
  }

  /**
   * Get mortgageRates
   * @return mortgageRates
   */
  @Valid 
  @Schema(name = "mortgageRates", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mortgageRates")
  public List<@Valid MortgageRate> getMortgageRates() {
    return mortgageRates;
  }

  public void setMortgageRates(List<@Valid MortgageRate> mortgageRates) {
    this.mortgageRates = mortgageRates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MortgageRatesResponse mortgageRatesResponse = (MortgageRatesResponse) o;
    return Objects.equals(this.mortgageRates, mortgageRatesResponse.mortgageRates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mortgageRates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MortgageRatesResponse {\n");
    sb.append("    mortgageRates: ").append(toIndentedString(mortgageRates)).append("\n");
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

