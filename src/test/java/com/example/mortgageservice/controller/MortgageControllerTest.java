package com.example.mortgageservice.controller;

import com.example.mortgageservice.controller.dto.MortgageRate;
import com.example.mortgageservice.controller.dto.MortgageRatesResponse;
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
}
