package com.example.mortgageservice.rule;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
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
        if (loanAmount.compareTo(homeValue) > 0) {
            return "Loan amount cannot exceed home value";
        }
        return "";
    }
}
