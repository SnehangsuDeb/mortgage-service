package com.example.mortgageservice.controller;

import com.example.mortgageservice.controller.dto.InterestRatesResponse;
import com.example.mortgageservice.controller.dto.MortgageCheckRequest;
import com.example.mortgageservice.controller.dto.MortgageCheckResponse;
import com.example.mortgageservice.controller.error.ApiError;
import com.example.mortgageservice.service.MortgageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MortgageController {

    private final MortgageService mortgageService;

    public MortgageController(MortgageService mortgageService) {
        this.mortgageService = mortgageService;
    }

    /**
     * GET /api/interest-rates : List available interest rates
     *
     * @return Invalid input (status code 400)
     *         or Unauthorized (status code 401)
     *         or Forbidden (status code 403)
     *         or Interest rates not found (status code 404)
     *         or Too many requests (status code 429)
     *         or Internal server error (status code 500)
     *         or Service unavailable (status code 503)
     *         or No content (status code 204)
     *         or OK (status code 200)
     */
    @Operation(
            operationId = "getInterestRates",
            summary = "List available interest rates",
            tags = { "Mortgage" },
            responses = {
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Interest rates not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "429", description = "Too many requests", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "503", description = "Service unavailable", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = InterestRatesResponse.class))
                    })
            }
    )
    @RequestMapping(method = RequestMethod.GET, value = "/interest-rates")
    public ResponseEntity<InterestRatesResponse> getInterestRates() {
        return ResponseEntity.ok(mortgageService.getInterestRates());

    }


    /**
     * POST /api/mortgage-check : Evaluate mortgage check
     *
     * @param mortgageCheckRequest  (required)
     * @return Mortgage Details (status code 200)
     *         or Invalid input (status code 400)
     *         or Unauthorized (status code 401)
     *         or Forbidden (status code 403)
     *         or Unprocessable entity (status code 422)
     *         or Too many requests (status code 429)
     *         or Internal server error (status code 500)
     *         or Service unavailable (status code 503)
     */
    @Operation(
            operationId = "mortgageCheck",
            summary = "Evaluate mortgage check",
            tags = { "Mortgage" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Mortgage Details", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = MortgageCheckResponse.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "422", description = "Unprocessable entity", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "429", description = "Too many requests", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "503", description = "Service unavailable", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(method = RequestMethod.POST, value = "/mortgage-check")

    public ResponseEntity<MortgageCheckResponse> mortgageCheck(
            @Parameter(name = "MortgageCheckRequest", description = "", required = true) @Valid @RequestBody MortgageCheckRequest mortgageCheckRequest
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }
}
