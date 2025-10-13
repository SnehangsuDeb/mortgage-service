package com.example.mortgageservice.service;

import com.example.mortgageservice.controller.dto.Amount;
import com.example.mortgageservice.controller.dto.MortgageCheckRequest;
import com.example.mortgageservice.controller.dto.MortgageCheckResponse;
import com.example.mortgageservice.controller.dto.MortgageRate;
import com.example.mortgageservice.controller.dto.MortgageRatesResponse;
import com.example.mortgageservice.mapper.MortgageMapper;
import com.example.mortgageservice.repository.MortgageRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MortgageServiceCheckTest {

    private MortgageRateRepository mortgageRateRepository;
    private MortgageMapper mortgageMapper;
    private MortgageRuleService mortgageRuleService;

    private MortgageService service;

    @BeforeEach
    void setUp() {
        mortgageRateRepository = mock(MortgageRateRepository.class);
        mortgageMapper = mock(MortgageMapper.class);
        mortgageRuleService = mock(MortgageRuleService.class);
        service = new MortgageService(mortgageRateRepository, mortgageMapper, mortgageRuleService);
    }

    /*@Test
    void mortgageCheck_returnsMonthlyPayment_whenRateAndTenurePresent() {
        // Given
        MortgageCheckRequest request = new MortgageCheckRequest(
                new Amount(new BigDecimal("5000")),
                30,
                new Amount(new BigDecimal("320000")),
                new Amount(new BigDecimal("400000"))
        );

        // Mock validation passes
        when(mortgageRuleService.requestedLoanValidation(
                request.getIncome().getValue(), request.getLoanValue().getValue(), request.getHomeValue().getValue()
        )).thenReturn(null);

        // Mapper returns a single rate matching the requested tenure
        MortgageRatesResponse response = new MortgageRatesResponse();
        response.setMortgageRates(Collections.singletonList(new MortgageRate(6.0, 30, MortgageRate.InterestTypeEnum.FIXED)));
        when(mortgageMapper.mapMortgageRates(anyList())).thenReturn(response);

        // When
        MortgageCheckResponse out = service.mortgageCheck(request);

        // Then: expected monthly payment using standard formula ~ 1919.81
        assertEquals(true, out.getEligible());
        assertEquals(1919.81, out.getMortgageAmountMonthly(), 0.01);
        verify(mortgageRuleService, times(1)).requestedLoanValidation(
                request.getIncome().getValue(), request.getLoanValue().getValue(), request.getHomeValue().getValue()
        );
        verify(mortgageMapper, atLeastOnce()).mapMortgageRates(anyList());
        verifyNoInteractions(mortgageRateRepository);
    }*/
}
