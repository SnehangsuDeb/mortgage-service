package com.example.mortgageservice.controller;

import com.example.mortgageservice.controller.dto.MortgageRatesResponse;
import com.example.mortgageservice.controller.dto.MortgageCheckRequest;
import com.example.mortgageservice.controller.dto.MortgageCheckResponse;
import com.example.mortgageservice.controller.exceptions.ApiError;
import com.example.mortgageservice.service.MortgageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * @return OK (status code 200)
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
                            @Content(mediaType = "application/json", schema = @Schema(implementation = MortgageRatesResponse.class))
                    })
            }
    )
    @GetMapping("/interest-rates")
    public ResponseEntity<MortgageRatesResponse> getInterestRates() {
        return ResponseEntity.ok(mortgageService.getInterestRates());

    }


    /**
     * POST /api/mortgage-check : Evaluate mortgage check
     *
     * @param mortgageCheckRequest  (required)
     * @return Mortgage Details (status code 200)
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
    @PostMapping("/mortgage-checking")
    public ResponseEntity<MortgageCheckResponse> mortgageCheck(@Valid @RequestBody MortgageCheckRequest mortgageCheckRequest
    ) {
        return ResponseEntity.ok(mortgageService.mortgageCheck(mortgageCheckRequest));

    }
}
