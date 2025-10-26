package com.example.mortgageservice.controller;

import com.example.mortgageservice.model.MortgageCheckRequest;
import com.example.mortgageservice.model.MortgageCheckResponse;
import com.example.mortgageservice.model.MortgageRate;
import com.example.mortgageservice.model.MortgageRatesResponse;
import com.example.mortgageservice.model.InterestTypeEnum;
import com.example.mortgageservice.ratelimit.RateLimiterService;
import com.example.mortgageservice.service.MortgageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MortgageController.class)
class MortgageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MortgageService mortgageService;
    @MockitoBean
    private RateLimiterService rateLimiterService;

    @SneakyThrows
    @Test
    void getInterestRates_returnsOkWithPayload() {
        var response = new MortgageRatesResponse(
                List.of(new MortgageRate(5.25, 15, InterestTypeEnum.FIXED))
        );

        when(mortgageService.getInterestRates()).thenReturn(response);

        mockMvc.perform(get("/api/interest-rates").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mortgageRates[0].rate").value(5.25))
                .andExpect(jsonPath("$.mortgageRates[0].tenure").value(15))
                .andExpect(jsonPath("$.mortgageRates[0].interestType").value("FIXED"));
    }

    @SneakyThrows
    @Test
    void getInterestRates_returnsOkWithEmptyArray() {
        var response = new MortgageRatesResponse(List.of());
        when(mortgageService.getInterestRates()).thenReturn(response);

        mockMvc.perform(get("/api/interest-rates").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mortgageRates").isArray())
                .andExpect(jsonPath("$.mortgageRates.length()").value(0));
    }

    @SneakyThrows
    @Test
    void mortgageCheck_returnsOkWithPayload() {
        var request = new MortgageCheckRequest(
                BigDecimal.valueOf(10000),
                30,
                BigDecimal.valueOf(20560),
                BigDecimal.valueOf(400000)
        );

        var response = new MortgageCheckResponse(true, 207.18);
        when(mortgageService.mortgageCheck(request)).thenReturn(response);

        mockMvc.perform(post("/api/mortgage-checking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.eligible").value(true))
                .andExpect(jsonPath("$.mortgageAmountMonthly").value(207.18));
    }

    @SneakyThrows
    @Test
    void mortgageCheck_returnsBadRequest_whenValidationFailsWithNullMaturityPeriod() {
        var invalidRequest = new MortgageCheckRequest(
                BigDecimal.valueOf(10000),
                null,
                BigDecimal.valueOf(20000),
                BigDecimal.valueOf(400000)
        );

        mockMvc.perform(post("/api/mortgage-checking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(mortgageService, never()).mortgageCheck(any());
    }

    @SneakyThrows
    @Test
    void mortgageCheck_returnsInternalServerError_whenServiceThrowsInternalServerError() {
        var request = new MortgageCheckRequest(
                BigDecimal.valueOf(10000),
                30,
                BigDecimal.valueOf(20000),
                BigDecimal.valueOf(400000)
        );
        when(mortgageService.mortgageCheck(any())).thenThrow(new RuntimeException("Something went wrong"));

        mockMvc.perform(post("/api/mortgage-checking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void getInterestRates_returnsInternalServerError_whenServiceThrowsInternalServerError() {
        when(mortgageService.getInterestRates()).thenThrow(new RuntimeException("down"));

        mockMvc.perform(get("/api/interest-rates").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
