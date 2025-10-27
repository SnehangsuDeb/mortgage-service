package com.example.mortgageservice.service;

import com.example.mortgageservice.exceptions.BadRequestException;
import com.example.mortgageservice.rule.MortgageRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MortgageRuleService {
    private final List<MortgageRule> rules;

    public void requestedLoanValidation(BigDecimal income, BigDecimal loanAmount, BigDecimal homeValue){
        for (MortgageRule rule : rules) {
            String error = rule.validate(income, loanAmount, homeValue);
            if (error != null) {
                throw new BadRequestException(error);
            }
        }
    }
}
