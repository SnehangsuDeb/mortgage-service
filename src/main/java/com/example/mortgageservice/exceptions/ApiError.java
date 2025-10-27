package com.example.mortgageservice.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

/**
 * Standard error response
 */

@Schema(name = "ApiError", description = "Standard error response")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  private Integer status;

  private String cause;

  private String message;

  private String path;
}
