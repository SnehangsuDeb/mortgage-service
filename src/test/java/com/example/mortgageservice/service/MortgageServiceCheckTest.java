package com.example.mortgageservice.service;

import com.example.mortgageservice.model.MortgageCheckRequest;
import com.example.mortgageservice.model.MortgageCheckResponse;
import com.example.mortgageservice.model.MortgageRate;
import com.example.mortgageservice.model.MortgageRatesResponse;
import com.example.mortgageservice.mapper.MortgageMapper;
import com.example.mortgageservice.repository.MortgageRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
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

    @Test
    void mortgageCheck_returnsMonthlyPayment_whenRateAndTenurePresent() {
        MortgageCheckRequest request = new MortgageCheckRequest(
                new BigDecimal("5000"),
                30,
                new BigDecimal("320000"),
                new BigDecimal("400000")
        );

        when(mortgageRuleService.requestedLoanValidation(
                request.getIncome(), request.getLoanValue(), request.getHomeValue()
        )).thenReturn(null);

        MortgageRatesResponse response = new MortgageRatesResponse();
        response.setMortgageRates(Collections.singletonList(new MortgageRate(6.0, 30, MortgageRate.InterestTypeEnum.FIXED)));
        when(mortgageMapper.mapMortgageRates(anyList())).thenReturn(response);

        MortgageCheckResponse out = service.mortgageCheck(request);
        assertEquals(true, out.getEligible());
        assertEquals(1918.56, out.getMortgageAmountMonthly(), 0.01);
        verify(mortgageRuleService, times(1)).requestedLoanValidation(
                request.getIncome(), request.getLoanValue(), request.getHomeValue()
        );
        verify(mortgageMapper, atLeastOnce()).mapMortgageRates(anyList());
        verify(mortgageRateRepository, times(1)).getRateByMortgagePeriod(30);
    }
}
