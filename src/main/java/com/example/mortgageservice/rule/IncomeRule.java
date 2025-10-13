package com.example.mortgageservice.rule;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class IncomeRule implements MortgageRule{
    @Override
    public String validate(BigDecimal income, BigDecimal loanAmount, BigDecimal homeValue) {
        if (income == null) {
            return "Income is required";
        }
        if (loanAmount == null) {
            return "Loan amount is required";
        }
        if (income.signum() <= 0) {
            return "Income must be greater than 0";
        }
        if (loanAmount.signum() <= 0) {
            return "Loan amount must be greater than 0";
        }

        BigDecimal maxAllowed = income.multiply(BigDecimal.valueOf(4));
        if (loanAmount.compareTo(maxAllowed) > 0) {
            return "Loan amount cannot exceed 4 times the income";
        }

        return "";
    }
}
