package com.example.mortgageservice.service;

import com.example.mortgageservice.controller.dto.MortgageCheckRequest;
import com.example.mortgageservice.controller.dto.MortgageCheckResponse;
import com.example.mortgageservice.controller.dto.MortgageRate;
import com.example.mortgageservice.controller.exceptions.NotFoundException;
import com.example.mortgageservice.mapper.MortgageMapper;
import com.example.mortgageservice.controller.dto.MortgageRatesResponse;
import com.example.mortgageservice.repository.MortgageRateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class MortgageService {
    private final MortgageRateRepository mortgageRateRepository;
    private final MortgageMapper mortgageMapper;
    private final MortgageRuleService mortgageRuleService;

    public MortgageService(MortgageRateRepository mortgageRateRepository,
                           MortgageMapper mortgageMapper,
                           MortgageRuleService mortgageRuleService) {
        this.mortgageRateRepository = mortgageRateRepository;
        this.mortgageMapper = mortgageMapper;
        this.mortgageRuleService = mortgageRuleService;
    }

    public MortgageRatesResponse getInterestRates() {
        return mortgageMapper.mapMortgageRates(mortgageRateRepository.findAll());
    }

    private Optional<MortgageRatesResponse> getRateByMaturityPeriod(Integer maturityPeriod){
        return Optional.of(
                mortgageMapper.mapMortgageRates(
                        Collections.singletonList(mortgageRateRepository.getRateByMortgagePeriod(maturityPeriod))
                )
        );
    }

    public MortgageCheckResponse mortgageCheck(MortgageCheckRequest request){
        log.info("Mortgage check request received: {}", request);
        var validation = mortgageRuleService.requestedLoanValidation(
                request.getIncome().getValue(), request.getLoanValue().getValue(), request.getHomeValue().getValue());
        if (validation != null) {
            return new MortgageCheckResponse(false, null);
        }

        var rateResponse = getRateByMaturityPeriod(request.getMaturityPeriod())
                .orElseThrow(() -> new NotFoundException(
                        "No mortgage rates found for maturity period: " + request.getMaturityPeriod()));

        if (rateResponse.getMortgageRates().isEmpty()) {
            return new MortgageCheckResponse(false, null);
        } else {
            MortgageRate selectedRate = rateResponse.getMortgageRates().get(0);
            Double monthly = calculateMonthlyAmountFromTotalLoanAmount(request, selectedRate);
            return new MortgageCheckResponse(true, monthly);
        }
    }

    /**
     * Calculate the monthly payment for an amortizing loan.
     * Formula:
     *  - i = annualRate / 100 / 12
     *  - n = tenureYears * 12
     *  - if i == 0: payment = P / n
     *  - else: payment = P * i * (1 + i)^n / ((1 + i)^n - 1)
     */
    private Double calculateMonthlyAmountFromTotalLoanAmount(MortgageCheckRequest request, MortgageRate mortgageRate) {
        double principal = request.getLoanValue().getValue().doubleValue();
        int months = Math.toIntExact((long) mortgageRate.getTenure() * 12);
        if (months <= 0) {
            return null;
        }

        double monthlyRate = (mortgageRate.getRate() == null ? 0.0 : mortgageRate.getRate()) / 100.0 / 12.0;

        double payment;
        if (monthlyRate == 0.0) {
            payment = principal / months;
        } else {
            double factor = Math.pow(1.0 + monthlyRate, months);
            payment = principal * monthlyRate * factor / (factor - 1.0);
        }

        // Round to 2 decimal places
        return Math.round(payment * 100.0) / 100.0;
    }
}
