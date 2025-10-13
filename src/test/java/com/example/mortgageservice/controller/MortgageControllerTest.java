package com.example.mortgageservice.controller;

import com.example.mortgageservice.model.MortgageCheckRequest;
import com.example.mortgageservice.model.MortgageCheckResponse;
import com.example.mortgageservice.model.MortgageRate;
import com.example.mortgageservice.model.MortgageRatesResponse;
import com.example.mortgageservice.ratelimit.RateLimiterService;
import com.example.mortgageservice.ratelimit.RateLimitingFilter;
import com.example.mortgageservice.service.MortgageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MortgageController.class)
class MortgageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MortgageService mortgageService;
    @MockBean
    private RateLimiterService rateLimiterService;

    @Test
    void getInterestRates_returnsOkWithPayload() throws Exception {
        MortgageRatesResponse response = new MortgageRatesResponse();
        response.setMortgageRates(List.of(new MortgageRate(5.25, 15, MortgageRate.InterestTypeEnum.FIXED)));

        when(mortgageService.getInterestRates()).thenReturn(response);

        mockMvc.perform(get("/api/interest-rates").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mortgageRates[0].rate").value(5.25))
                .andExpect(jsonPath("$.mortgageRates[0].tenure").value(15))
                .andExpect(jsonPath("$.mortgageRates[0].interestType").value("FIXED"));
    }

    @Test
    void mortgageCheck_returnsOkWithPayload() throws Exception {
        MortgageCheckRequest request = new MortgageCheckRequest(
                new BigDecimal("10000"),
                30,
                new BigDecimal("20560"),
                new BigDecimal("400000")
        );

        MortgageCheckResponse response = new MortgageCheckResponse(true, 207.18);
        when(mortgageService.mortgageCheck(request)).thenReturn(response);

        mockMvc.perform(post("/api/mortgage-checking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.eligible").value(true))
                .andExpect(jsonPath("$.mortgageAmountMonthly").value(207.18));
    }
}
