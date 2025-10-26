package com.example.mortgageservice.service;

import com.example.mortgageservice.entities.MortgageRates;
import com.example.mortgageservice.mapper.MortgageMapper;
import com.example.mortgageservice.model.InterestTypeEnum;
import com.example.mortgageservice.model.MortgageCheckRequest;
import com.example.mortgageservice.model.MortgageRate;
import com.example.mortgageservice.model.MortgageRatesResponse;
import com.example.mortgageservice.repository.MortgageRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
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
        mortgageRuleService = mock(MortgageRuleService.class);

        service = new MortgageService(mortgageRateRepository, mortgageMapper, mortgageRuleService);
    }

    @Test
    void success_getInterestRates() {
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

    @Test
    void computesMonthlyPayment_whenRateFound_andTenurePositive() {
        var request = new MortgageCheckRequest(
                BigDecimal.valueOf(5000),
                30,
                BigDecimal.valueOf(320000),
                BigDecimal.valueOf(400000)
        );
        var entity = MortgageRates.builder().rate(6.0).mortgagePeriod(30).type("FIXED").build();
        when(mortgageRateRepository.getRateByMortgagePeriod(30)).thenReturn(entity);

        var mapped = new MortgageRatesResponse(List.of(new MortgageRate(6.0, 30, InterestTypeEnum.FIXED)));
        when(mortgageMapper.mapMortgageRates(List.of(entity))).thenReturn(mapped);

        var out = service.mortgageCheck(request);

        assertTrue(out.eligible());
        assertEquals(1918.56, out.mortgageAmountMonthly(), 0.01);
        verify(mortgageRuleService, times(1))
                .requestedLoanValidation(request.income(), request.loanValue(), request.homeValue());
        verify(mortgageRateRepository, times(1)).getRateByMortgagePeriod(30);
        verify(mortgageMapper, times(1)).mapMortgageRates(List.of(entity));
    }

    @Test
    void shouldReturnsZero_whenRateEntityMissing() {
        var request = new MortgageCheckRequest(
                BigDecimal.valueOf(4000), 15,
                BigDecimal.valueOf(150000), BigDecimal.valueOf(300000)
        );
        when(mortgageRateRepository.getRateByMortgagePeriod(15)).thenReturn(null);

        var out = service.mortgageCheck(request);

        assertTrue(out.eligible());
        assertEquals(0.0, out.mortgageAmountMonthly(), 0.0);
        verify(mortgageRuleService, times(1))
                .requestedLoanValidation(request.income(), request.loanValue(), request.homeValue());
        verify(mortgageRateRepository, times(1)).getRateByMortgagePeriod(15);
        verify(mortgageMapper, never()).mapMortgageRates(anyList());
    }

    @Test
    void shouldHandlesZeroInterestRate_correctly() {
        var request = new MortgageCheckRequest(
                BigDecimal.valueOf(4000), 30,
                BigDecimal.valueOf(360000), BigDecimal.valueOf(400000)
        );
        var entity = MortgageRates.builder().rate(0.0).mortgagePeriod(30).type("FIXED").build();
        when(mortgageRateRepository.getRateByMortgagePeriod(30)).thenReturn(entity);

        var mapped = new MortgageRatesResponse(List.of(new MortgageRate(0.0, 30, InterestTypeEnum.FIXED)));
        when(mortgageMapper.mapMortgageRates(List.of(entity))).thenReturn(mapped);

        var out = service.mortgageCheck(request);

        assertTrue(out.eligible());
        assertEquals(1000.0, out.mortgageAmountMonthly(), 0.0); // 360000 / (30*12)
        verify(mortgageRateRepository, times(1)).getRateByMortgagePeriod(30);
        verify(mortgageMapper, times(1)).mapMortgageRates(List.of(entity));
    }

    @Test
    void shouldReturnsZero_whenTenureIsZero() {
        var request = new MortgageCheckRequest(
                BigDecimal.valueOf(5000), 0,
                BigDecimal.valueOf(200000), BigDecimal.valueOf(300000)
        );
        when(mortgageRateRepository.getRateByMortgagePeriod(0)).thenReturn(null);

        var out = service.mortgageCheck(request);

        assertTrue(out.eligible());
        assertEquals(0.0, out.mortgageAmountMonthly(), 0.0);
        verify(mortgageRuleService, times(1))
                .requestedLoanValidation(request.income(), request.loanValue(), request.homeValue());
        verify(mortgageRateRepository, times(1)).getRateByMortgagePeriod(0);
        verify(mortgageMapper, never()).mapMortgageRates(anyList());
    }

    @Test
    void shouldReturnsZero_whenMapperProducesEmptyRates() {
        var request = new MortgageCheckRequest(
                BigDecimal.valueOf(5000), 20,
                BigDecimal.valueOf(200000), BigDecimal.valueOf(300000)
        );
        var entity = MortgageRates.builder().rate(5.0).mortgagePeriod(20).type("FIXED").build();
        when(mortgageRateRepository.getRateByMortgagePeriod(20)).thenReturn(entity);

        when(mortgageMapper.mapMortgageRates(List.of(entity)))
                .thenReturn(new MortgageRatesResponse(List.of())); // empty mapped list

        var out = service.mortgageCheck(request);

        assertTrue(out.eligible());
        assertEquals(0.0, out.mortgageAmountMonthly(), 0.0);
        verify(mortgageRateRepository, times(1)).getRateByMortgagePeriod(20);
        verify(mortgageMapper, times(1)).mapMortgageRates(List.of(entity));
    }

    @Test
    void shouldPropagatesException_fromRuleService_andSkipsRepoMapper() {
        var request = new MortgageCheckRequest(
                BigDecimal.valueOf(5000), 15,
                BigDecimal.valueOf(120000), BigDecimal.valueOf(300000)
        );
        doThrow(new RuntimeException("boom"))
                .when(mortgageRuleService)
                .requestedLoanValidation(any(), any(), any());

        var ex = assertThrows(RuntimeException.class, () -> service.mortgageCheck(request));
        assertEquals("boom", ex.getMessage());

        verifyNoInteractions(mortgageRateRepository, mortgageMapper);
    }
}
