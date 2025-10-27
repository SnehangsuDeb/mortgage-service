package com.example.mortgageservice.rule;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class HomeValueRuleTest {

    private final HomeValueRule rule = new HomeValueRule();

    @Test
    void returnsError_whenHomeValueIsNull() {
        var error = rule.validate(BigDecimal.ONE, BigDecimal.ONE, null);
        assertEquals("Home value is required", error);
    }

    @Test
    void returnsError_whenLoanAmountIsNull() {
        var error = rule.validate(BigDecimal.ONE, null, BigDecimal.TEN);
        assertEquals("Loan amount is required", error);
    }

    @Test
    void returnsError_whenHomeValueIsZeroOrNegative() {
        assertEquals("Home value must be greater than 0", rule.validate(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ZERO));
        assertEquals("Home value must be greater than 0", rule.validate(BigDecimal.ONE, BigDecimal.ONE, new BigDecimal("-1")));
    }

    @Test
    void returnsError_whenLoanAmountIsZeroOrNegative() {
        assertEquals("Loan amount must be greater than 0", rule.validate(BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.TEN));
        assertEquals("Loan amount must be greater than 0", rule.validate(BigDecimal.ONE, new BigDecimal("-10"), BigDecimal.TEN));
    }

    @Test
    void returnsError_whenLoanAmountExceedsHomeValue() {
        var loan = new BigDecimal("300001");
        var home = new BigDecimal("300000");
        var error = rule.validate(BigDecimal.TEN, loan, home);
        assertEquals("Loan amount cannot exceed home value", error);
    }

    @Test
    void passes_whenLoanAmountEqualsHomeValue_edgeCase() {
        var value = new BigDecimal("300000");
        var error = rule.validate(BigDecimal.ONE, value, value);
        assertNull(error);
    }

    @Test
    void passes_whenLoanAmountBelowHomeValue() {
        var error = rule.validate(BigDecimal.ONE, new BigDecimal("250000"), new BigDecimal("300000"));
        assertNull(error);
    }
}
