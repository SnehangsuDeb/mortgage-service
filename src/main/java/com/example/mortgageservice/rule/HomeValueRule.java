package com.example.mortgageservice.rule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class HomeValueRule implements MortgageRule{

    @Override
    public String validate(BigDecimal income, BigDecimal loanAmount, BigDecimal homeValue) {
        if (homeValue == null) {
            return "Home value is required";
        }
        if (loanAmount == null) {
            return "Loan amount is required";
        }
        if (homeValue.signum() <= 0) {
            return "Home value must be greater than 0";
        }
        if (loanAmount.signum() <= 0) {
            return "Loan amount must be greater than 0";
        }
        log.info("LoanAmount: {}, HomeValue: {}", loanAmount, homeValue);
        if (loanAmount.compareTo(homeValue) > 0) {
            return "Loan amount cannot exceed home value";
        }
        return "";
    }
}
