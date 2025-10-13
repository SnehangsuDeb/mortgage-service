package com.example.mortgageservice.service;

import com.example.mortgageservice.exceptions.BadRequestException;
import com.example.mortgageservice.rule.MortgageRule;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MortgageRuleService {
    private final List<MortgageRule> rules;

    public MortgageRuleService(List<MortgageRule> rules) {
        this.rules = rules;
    }

    public String requestedLoanValidation(BigDecimal income, BigDecimal loanAmount, BigDecimal homeValue){
        for (MortgageRule rule : rules) {
            String error = rule.validate(income, loanAmount, homeValue);
            if (error != null) {
                throw new BadRequestException(error);
            }
        }
        return null;
    }
}
