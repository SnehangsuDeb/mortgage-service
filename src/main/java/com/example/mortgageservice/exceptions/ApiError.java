package com.example.mortgageservice.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

/**
 * Standard error response
 */

@Schema(name = "ApiError", description = "Standard error response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-13T17:12:32.542554800+05:30[Asia/Calcutta]", comments = "Generator version: 7.7.0")
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

