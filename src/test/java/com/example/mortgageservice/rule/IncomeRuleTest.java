package com.example.mortgageservice.rule;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IncomeRuleTest {

    private final IncomeRule rule = new IncomeRule();

    @Test
    void returnsError_whenIncomeIsNull() {
        var error = rule.validate(null, BigDecimal.ONE, BigDecimal.TEN);
        assertEquals("Income is required", error);
    }

    @Test
    void returnsError_whenLoanAmountIsNull() {
        var error = rule.validate(BigDecimal.TEN, null, BigDecimal.TEN);
        assertEquals("Loan amount is required", error);
    }

    @Test
    void returnsError_whenIncomeIsZeroOrNegative() {
        assertEquals("Income must be greater than 0", rule.validate(BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.TEN));
        assertEquals("Income must be greater than 0", rule.validate(new BigDecimal("-1"), BigDecimal.ONE, BigDecimal.TEN));
    }

    @Test
    void returnsError_whenLoanAmountIsZeroOrNegative() {
        assertEquals("Loan amount must be greater than 0", rule.validate(BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.TEN));
        assertEquals("Loan amount must be greater than 0", rule.validate(BigDecimal.ONE, new BigDecimal("-5"), BigDecimal.TEN));
    }

    @Test
    void returnsError_whenLoanExceedsFourTimesIncome() {
        var income = new BigDecimal("1000");
        var loan = new BigDecimal("4000.01");
        var error = rule.validate(income, loan, BigDecimal.TEN);
        assertEquals("Loan amount cannot exceed 4 times the income", error);
    }

    @Test
    void passes_whenLoanEqualsFourTimesIncome_edgeCase() {
        var income = new BigDecimal("1000");
        var loan = new BigDecimal("4000"); // exactly 4x
        var error = rule.validate(income, loan, BigDecimal.TEN);
        assertNull(error);
    }

    @Test
    void passes_whenAllInputsValid_belowLimits() {
        var error = rule.validate(new BigDecimal("2500"), new BigDecimal("9999.99"), new BigDecimal("123456"));
        assertNull(error);
    }
}
