package com.example.mortgageservice.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

/**
 * Mortgage check request payload
 */

@Schema(name = "MortgageCheckRequest", description = "Mortgage check request payload")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-13T17:12:32.542554800+05:30[Asia/Calcutta]", comments = "Generator version: 7.7.0")
public class MortgageCheckRequest {

  private Amount income;

  private Integer maturityPeriod;

  private Amount loanValue;

  private Amount homeValue;

  public MortgageCheckRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MortgageCheckRequest(Amount income, Integer maturityPeriod, Amount loanValue, Amount homeValue) {
    this.income = income;
    this.maturityPeriod = maturityPeriod;
    this.loanValue = loanValue;
    this.homeValue = homeValue;
  }

  public MortgageCheckRequest income(Amount income) {
    this.income = income;
    return this;
  }

  /**
   * Get income
   * @return income
   */
  @NotNull @Valid 
  @Schema(name = "income", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("income")
  public Amount getIncome() {
    return income;
  }

  public void setIncome(Amount income) {
    this.income = income;
  }

  public MortgageCheckRequest maturityPeriod(Integer maturityPeriod) {
    this.maturityPeriod = maturityPeriod;
    return this;
  }

  /**
   * Maturity period in years
   * minimum: 1
   * @return maturityPeriod
   */
  @NotNull @Min(1) 
  @Schema(name = "maturityPeriod", example = "30", description = "Maturity period in years", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("maturityPeriod")
  public Integer getMaturityPeriod() {
    return maturityPeriod;
  }

  public void setMaturityPeriod(Integer maturityPeriod) {
    this.maturityPeriod = maturityPeriod;
  }

  public MortgageCheckRequest loanValue(Amount loanValue) {
    this.loanValue = loanValue;
    return this;
  }

  /**
   * Get loanValue
   * @return loanValue
   */
  @NotNull @Valid 
  @Schema(name = "loanValue", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("loanValue")
  public Amount getLoanValue() {
    return loanValue;
  }

  public void setLoanValue(Amount loanValue) {
    this.loanValue = loanValue;
  }

  public MortgageCheckRequest homeValue(Amount homeValue) {
    this.homeValue = homeValue;
    return this;
  }

  /**
   * Get homeValue
   * @return homeValue
   */
  @NotNull @Valid 
  @Schema(name = "homeValue", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("homeValue")
  public Amount getHomeValue() {
    return homeValue;
  }

  public void setHomeValue(Amount homeValue) {
    this.homeValue = homeValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MortgageCheckRequest mortgageCheckRequest = (MortgageCheckRequest) o;
    return Objects.equals(this.income, mortgageCheckRequest.income) &&
        Objects.equals(this.maturityPeriod, mortgageCheckRequest.maturityPeriod) &&
        Objects.equals(this.loanValue, mortgageCheckRequest.loanValue) &&
        Objects.equals(this.homeValue, mortgageCheckRequest.homeValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(income, maturityPeriod, loanValue, homeValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MortgageCheckRequest {\n");
    sb.append("    income: ").append(toIndentedString(income)).append("\n");
    sb.append("    maturityPeriod: ").append(toIndentedString(maturityPeriod)).append("\n");
    sb.append("    loanValue: ").append(toIndentedString(loanValue)).append("\n");
    sb.append("    homeValue: ").append(toIndentedString(homeValue)).append("\n");
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

