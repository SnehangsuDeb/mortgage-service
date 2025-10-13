package com.example.mortgageservice.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

/**
 * Interest rate details
 */

@Schema(name = "InterestRate", description = "Interest rate details")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-13T17:12:32.542554800+05:30[Asia/Calcutta]", comments = "Generator version: 7.7.0")
public class InterestRate {

  private Double rate;

  private Integer tenure;

  /**
   * Type of interest
   */
  public enum InterestTypeEnum {
    FIXED("FIXED"),
    
    VARIABLE("VARIABLE");

    private String value;

    InterestTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static InterestTypeEnum fromValue(String value) {
      for (InterestTypeEnum b : InterestTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private InterestTypeEnum interestType;

  public InterestRate() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InterestRate(Double rate, Integer tenure, InterestTypeEnum interestType) {
    this.rate = rate;
    this.tenure = tenure;
    this.interestType = interestType;
  }

  public InterestRate rate(Double rate) {
    this.rate = rate;
    return this;
  }

  /**
   * Percentage value (0-100)
   * minimum: 0
   * maximum: 100
   * @return rate
   */
  @NotNull @DecimalMin("0") @DecimalMax("100") 
  @Schema(name = "rate", example = "5.75", description = "Percentage value (0-100)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("rate")
  public Double getRate() {
    return rate;
  }

  public void setRate(Double rate) {
    this.rate = rate;
  }

  public InterestRate tenure(Integer tenure) {
    this.tenure = tenure;
    return this;
  }

  /**
   * Tenure in years
   * minimum: 1
   * @return tenure
   */
  @NotNull @Min(1) 
  @Schema(name = "tenure", example = "30", description = "Tenure in years", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tenure")
  public Integer getTenure() {
    return tenure;
  }

  public void setTenure(Integer tenure) {
    this.tenure = tenure;
  }

  public InterestRate interestType(InterestTypeEnum interestType) {
    this.interestType = interestType;
    return this;
  }

  /**
   * Type of interest
   * @return interestType
   */
  @NotNull 
  @Schema(name = "interestType", example = "FIXED", description = "Type of interest", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("interestType")
  public InterestTypeEnum getInterestType() {
    return interestType;
  }

  public void setInterestType(InterestTypeEnum interestType) {
    this.interestType = interestType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InterestRate interestRate = (InterestRate) o;
    return Objects.equals(this.rate, interestRate.rate) &&
        Objects.equals(this.tenure, interestRate.tenure) &&
        Objects.equals(this.interestType, interestRate.interestType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rate, tenure, interestType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InterestRate {\n");
    sb.append("    rate: ").append(toIndentedString(rate)).append("\n");
    sb.append("    tenure: ").append(toIndentedString(tenure)).append("\n");
    sb.append("    interestType: ").append(toIndentedString(interestType)).append("\n");
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

