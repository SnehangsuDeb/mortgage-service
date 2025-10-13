package com.example.mortgageservice.service;

import com.example.mortgageservice.controller.exceptions.BadRequestException;
import com.example.mortgageservice.rule.MortgageRule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MortgageRuleServiceTest {

    /*@Test
    void requestedLoanValidation_passes_whenAllRulesReturnEmpty() {
        MortgageRule rule1 = mock(MortgageRule.class);
        MortgageRule rule2 = mock(MortgageRule.class);
        when(rule1.validate(new BigDecimal("5000"), new BigDecimal("320000"), new BigDecimal("400000"))).thenReturn("");
        when(rule2.validate(new BigDecimal("5000"), new BigDecimal("320000"), new BigDecimal("400000"))).thenReturn("");

        MortgageRuleService service = new MortgageRuleService(List.of(rule1, rule2));

        assertDoesNotThrow(() ->
                service.requestedLoanValidation(new BigDecimal("5000"), new BigDecimal("320000"), new BigDecimal("400000")));
    }*/

    @Test
    void requestedLoanValidation_throwsBadRequest_whenAnyRuleReturnsError() {
        MortgageRule rule1 = mock(MortgageRule.class);
        MortgageRule rule2 = mock(MortgageRule.class);
        when(rule1.validate(new BigDecimal("5000"), new BigDecimal("320000"), new BigDecimal("400000"))).thenReturn("");
        when(rule2.validate(new BigDecimal("5000"), new BigDecimal("320000"), new BigDecimal("400000")))
                .thenReturn("Loan amount cannot exceed 4 times the income");

        MortgageRuleService service = new MortgageRuleService(List.of(rule1, rule2));

        assertThrows(BadRequestException.class, () ->
                service.requestedLoanValidation(new BigDecimal("5000"), new BigDecimal("320000"), new BigDecimal("400000")));
    }
}
