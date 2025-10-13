package com.example.mortgageservice.controller;

import com.example.mortgageservice.controller.dto.InterestRate;
import com.example.mortgageservice.controller.dto.InterestRatesResponse;
import com.example.mortgageservice.service.MortgageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MortgageController.class)
class MortgageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MortgageService mortgageService;

    @Test
    void getInterestRates_returnsOkWithPayload() throws Exception {
        InterestRatesResponse response = new InterestRatesResponse();
        response.setInterestRates(List.of(new InterestRate(5.25, 15, InterestRate.InterestTypeEnum.FIXED)));

        when(mortgageService.getInterestRates()).thenReturn(response);

        mockMvc.perform(get("/api/interest-rates").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.interestRates[0].rate").value(5.25))
                .andExpect(jsonPath("$.interestRates[0].tenure").value(15))
                .andExpect(jsonPath("$.interestRates[0].interestType").value("FIXED"));
    }
}
