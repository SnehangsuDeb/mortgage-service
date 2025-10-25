package com.example.mortgageservice.service;

import com.example.mortgageservice.model.MortgageCheckRequest;
import com.example.mortgageservice.model.MortgageCheckResponse;
import com.example.mortgageservice.model.MortgageRate;
import com.example.mortgageservice.mapper.MortgageMapper;
import com.example.mortgageservice.model.MortgageRatesResponse;
import com.example.mortgageservice.repository.MortgageRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MortgageService {
    private final MortgageRateRepository mortgageRateRepository;
    private final MortgageMapper mortgageMapper;
    private final MortgageRuleService mortgageRuleService;

    public MortgageRatesResponse getInterestRates() {
        return mortgageMapper.mapMortgageRates(mortgageRateRepository.findAll());
    }

    public MortgageCheckResponse mortgageCheck(MortgageCheckRequest request){
        log.info("Mortgage check request received: {}", request);
        mortgageRuleService.requestedLoanValidation(request.income(), request.loanValue(), request.homeValue());

        var monthlyPayment = Optional.ofNullable(mortgageRateRepository.getRateByMortgagePeriod(request.maturityPeriod()))
                .map(rateEntity -> mortgageMapper.mapMortgageRates(List.of(rateEntity)))
                .map(MortgageRatesResponse::mortgageRates)
                .flatMap(rates -> rates.stream().findFirst())
                .map(MortgageRate::rate)
                .map(annualRate -> calculateMonthlyAmountFromTotalLoanAmount(
                        request.loanValue().doubleValue(),
                        annualRate,
                        request.maturityPeriod()
                ))
                .orElse(0.0);

        return new MortgageCheckResponse(true, monthlyPayment);

    }

    /**
     * Calculate the monthly payment for an amortizing loan.
     * Formula:
     *  - i = annualRate / 100 / 12
     *  - n = tenureYears * 12
     *  - if i == 0: payment = P / n
     *  - else: payment = P * i * (1 + i)^n / ((1 + i)^n - 1)
     */
    private Double calculateMonthlyAmountFromTotalLoanAmount(double principal, double annualRate, int tenure) {
        int months = Math.toIntExact((long) tenure * 12);
        if (months <= 0) {
            return 0.0;
        }

        double monthlyRate = annualRate / 100.0 / 12.0;
        double payment;
        if (monthlyRate == 0.0) {
            payment = principal / months;
        } else {
            double factor = Math.pow(1.0 + monthlyRate, months);
            payment = principal * monthlyRate * factor / (factor - 1.0);
        }
        return Math.round(payment * 100.0) / 100.0;
    }
}
