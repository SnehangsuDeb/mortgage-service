package com.example.mortgageservice.service;

import com.example.mortgageservice.entities.MortgageRates;
import com.example.mortgageservice.mapper.MortgageMapper;
import com.example.mortgageservice.model.InterestTypeEnum;
import com.example.mortgageservice.model.MortgageCheckRequest;
import com.example.mortgageservice.model.MortgageRate;
import com.example.mortgageservice.model.MortgageRatesResponse;
import com.example.mortgageservice.repository.MortgageRateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MortgageServiceCheckTest {

    @Mock
    private MortgageRateRepository mortgageRateRepository;
    @Mock
    private MortgageMapper mortgageMapper;
    @Mock
    private MortgageRuleService mortgageRuleService;

    @InjectMocks
    private MortgageService service;

    @Test
    void mortgageCheck_returnsMonthlyPayment_whenRateAndTenurePresent() {
        var request = new MortgageCheckRequest(
                BigDecimal.valueOf(5000),
                30,
                BigDecimal.valueOf(320000),
                BigDecimal.valueOf(400000)
        );

        var entity = MortgageRates.builder()
                .rate(6.0).mortgagePeriod(30).type("FIXED").build();
        when(mortgageRateRepository.getRateByMortgagePeriod(30)).thenReturn(entity);

        var response = new MortgageRatesResponse(
                List.of(new MortgageRate(6.0, 30, InterestTypeEnum.FIXED))
        );
        when(mortgageMapper.mapMortgageRates(List.of(entity))).thenReturn(response);

        var out = service.mortgageCheck(request);
        assertTrue(out.eligible());
        assertEquals(1918.56, out.mortgageAmountMonthly(), 0.01);

        verify(mortgageRuleService, times(1)).requestedLoanValidation(
                request.income(), request.loanValue(), request.homeValue()
        );
        verify(mortgageMapper, times(1)).mapMortgageRates(List.of(entity));
        verify(mortgageRateRepository, times(1)).getRateByMortgagePeriod(30);
    }

    @Test
    void mortgageCheck_returnsZero_whenRateEntityMissing() {
        var request = new MortgageCheckRequest(
                BigDecimal.valueOf(4000), 15,
                BigDecimal.valueOf(150000), BigDecimal.valueOf(300000)
        );
        when(mortgageRateRepository.getRateByMortgagePeriod(15)).thenReturn(null);

        var out = service.mortgageCheck(request);

        assertTrue(out.eligible());
        assertEquals(0.0, out.mortgageAmountMonthly(), 0.0);
        verify(mortgageMapper, never()).mapMortgageRates(anyList());
        verify(mortgageRateRepository, times(1)).getRateByMortgagePeriod(15);
    }

    @Test
    void mortgageCheck_handlesZeroInterestRate_correctly() {
        var request = new MortgageCheckRequest(
                BigDecimal.valueOf(4000), 30,
                BigDecimal.valueOf(360000), BigDecimal.valueOf(400000)
        );

        var entity = MortgageRates.builder()
                .rate(0.0).mortgagePeriod(30).type("FIXED").build();
        when(mortgageRateRepository.getRateByMortgagePeriod(30)).thenReturn(entity);

        var response = new MortgageRatesResponse(
                List.of(new MortgageRate(0.0, 30, InterestTypeEnum.FIXED))
        );
        when(mortgageMapper.mapMortgageRates(List.of(entity))).thenReturn(response);

        var out = service.mortgageCheck(request);

        // 360,000 / (30*12) = 1000.0 when interest is zero
        assertTrue(out.eligible());
        assertEquals(1000.0, out.mortgageAmountMonthly(), 0.0);
        verify(mortgageRateRepository, times(1)).getRateByMortgagePeriod(30);
        verify(mortgageMapper, times(1)).mapMortgageRates(List.of(entity));
    }

    @Test
    void mortgageCheck_returnsZero_whenTenureIsZero() {
        var request = new MortgageCheckRequest(
                BigDecimal.valueOf(5000), 0,
                BigDecimal.valueOf(200000), BigDecimal.valueOf(300000)
        );

        when(mortgageRateRepository.getRateByMortgagePeriod(0)).thenReturn(null);

        var out = service.mortgageCheck(request);

        assertTrue(out.eligible());
        assertEquals(0.0, out.mortgageAmountMonthly(), 0.0);
        verify(mortgageRateRepository, times(1)).getRateByMortgagePeriod(0);
        verify(mortgageMapper, never()).mapMortgageRates(anyList());
    }
}
