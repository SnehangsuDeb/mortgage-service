package com.example.mortgageservice.service;

import com.example.mortgageservice.model.MortgageRatesResponse;
import com.example.mortgageservice.entities.MortgageRates;
import com.example.mortgageservice.mapper.MortgageMapper;
import com.example.mortgageservice.repository.MortgageRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class MortgageServiceTest {

    private MortgageRateRepository mortgageRateRepository;
    private MortgageRuleService mortgageRuleService;
    private MortgageMapper mortgageMapper;

    private MortgageService service;

    @BeforeEach
    void setUp() {
        mortgageRateRepository = mock(MortgageRateRepository.class);
        mortgageMapper = mock(MortgageMapper.class);
        mortgageRuleService=mock(MortgageRuleService.class);

        service = new MortgageService(mortgageRateRepository, mortgageMapper, mortgageRuleService);
    }

    @Test
    void getInterestRates_fetchesEntities_andMapsToResponse() {
        var entities = List.of(
                MortgageRates.builder().rate(3.9).mortgagePeriod(10).type("FIXED").build()
        );
        var mapped = new MortgageRatesResponse(List.of());

        when(mortgageRateRepository.findAll()).thenReturn(entities);
        when(mortgageMapper.mapMortgageRates(ArgumentMatchers.eq(entities))).thenReturn(mapped);

        var result = service.getInterestRates();

        assertSame(mapped, result);
        verify(mortgageRateRepository, times(1)).findAll();
        verify(mortgageMapper, times(1)).mapMortgageRates(entities);
        verifyNoMoreInteractions(mortgageRateRepository, mortgageMapper);
    }
}
