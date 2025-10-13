package com.example.mortgageservice.rule;

import java.math.BigDecimal;

public interface MortgageRule {
    String validate(BigDecimal income, BigDecimal loanAmount, BigDecimal homeValue);
}
