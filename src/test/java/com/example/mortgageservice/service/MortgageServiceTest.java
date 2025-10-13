package com.example.mortgageservice.service;

import com.example.mortgageservice.controller.dto.InterestRatesResponse;
import com.example.mortgageservice.entities.InterestRates;
import com.example.mortgageservice.mapper.MortgageMapper;
import com.example.mortgageservice.repository.InterestRepository;
import com.example.mortgageservice.repository.MortgageRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class MortgageServiceTest {

    private MortgageRateRepository mortgageRateRepository;
    private InterestRepository interestRepository;
    private MortgageMapper mortgageMapper;

    private MortgageService service;

    @BeforeEach
    void setUp() {
        mortgageRateRepository = mock(MortgageRateRepository.class);
        interestRepository = mock(InterestRepository.class);
        mortgageMapper = mock(MortgageMapper.class);
        service = new MortgageService(mortgageRateRepository, interestRepository, mortgageMapper);
    }

    @Test
    void getInterestRates_fetchesEntities_andMapsToResponse() {
        List<InterestRates> entities = List.of(
                InterestRates.builder().rate(3.9).tenure(10).type("FIXED").build()
        );
        InterestRatesResponse mapped = new InterestRatesResponse();

        when(interestRepository.findAll()).thenReturn(entities);
        when(mortgageMapper.mapInterestRates(ArgumentMatchers.eq(entities))).thenReturn(mapped);

        InterestRatesResponse result = service.getInterestRates();

        assertSame(mapped, result);
        verify(interestRepository, times(1)).findAll();
        verify(mortgageMapper, times(1)).mapInterestRates(entities);
        verifyNoInteractions(mortgageRateRepository);
    }
}
