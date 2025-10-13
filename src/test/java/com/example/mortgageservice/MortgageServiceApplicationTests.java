package com.example.mortgageservice;

import com.example.mortgageservice.repository.InterestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MortgageServiceApplicationTests {

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        // Verifies the Spring context starts successfully
    }

    @Test
    void h2IsSeededWithInterestRates() {
        assertThat(interestRepository).isNotNull();
        assertThat(interestRepository.findAll()).isNotEmpty();
    }

    @Test
    void getInterestRates_returnsOkAndJsonArray() throws Exception {
        mockMvc.perform(get("/api/interest-rates"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.interestRates").isArray());
    }
}
