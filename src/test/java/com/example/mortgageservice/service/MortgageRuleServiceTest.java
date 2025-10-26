package com.example.mortgageservice.service;

import com.example.mortgageservice.exceptions.BadRequestException;
import com.example.mortgageservice.rule.MortgageRule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MortgageRuleServiceTest {

    @Test
    void shouldNotThrowAnyErrors_WhenNullReturnedFromMortgageRule() {
        var rule1 = mock(MortgageRule.class);
        var rule2 = mock(MortgageRule.class);
        when(rule1.validate(any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class))).thenReturn(null);
        when(rule2.validate(any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class))).thenReturn(null);

        var service = new MortgageRuleService(List.of(rule1, rule2));

        assertDoesNotThrow(() ->
                service.requestedLoanValidation(new BigDecimal("5000"), new BigDecimal("15000"), new BigDecimal("400000")));
    }

    @Test
    void shouldReturnBadRequest_WhenIncomeRuleFails() {
        var rule1 = mock(MortgageRule.class);
        var rule2 = mock(MortgageRule.class);
        when(rule1.validate(any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class))).thenReturn(null);
        when(rule2.validate(any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn("Loan amount cannot exceed 4 times the income");

        var service = new MortgageRuleService(List.of(rule1, rule2));

        assertThrows(BadRequestException.class, () ->
                service.requestedLoanValidation(new BigDecimal("5000"), new BigDecimal("320000"), new BigDecimal("400000")));
    }

    @Test
    void shouldNotFailValidation_whenNoRulesAreRegistered() {
        var service = new MortgageRuleService(List.of());
        assertDoesNotThrow(() -> service.requestedLoanValidation(
                new BigDecimal("1000"), new BigDecimal("5000"), new BigDecimal("10000")));
    }

    @Test
    void shouldStopOnFirstRuleFailure_whenMultipleRulesAreRegistered() {
        var callOrder = new ArrayList<String>();
        var thirdInvocations = new AtomicInteger(0);

        var service = getMortgageRuleService(callOrder, thirdInvocations);

        var ex = assertThrows(BadRequestException.class, () ->
                service.requestedLoanValidation(new BigDecimal("5000"), new BigDecimal("15000"), new BigDecimal("400000")));

        assertEquals("second-fails", ex.getMessage());
        assertEquals(2, callOrder.size(), "Should stop evaluating rules at first failure");
        assertEquals("first", callOrder.getFirst());
        assertEquals("second", callOrder.getLast());
        assertEquals(0, thirdInvocations.get(), "Third rule must not be invoked after failure");
    }

    private static MortgageRuleService getMortgageRuleService(ArrayList<String> callOrder, AtomicInteger thirdInvocations) {
        MortgageRule first = (income, loan, home) -> {
            callOrder.add("first");
            return null;
        };
        MortgageRule second = (income, loan, home) -> {
            callOrder.add("second");
            return "second-fails";
        };
        MortgageRule third = (income, loan, home) -> {
            callOrder.add("third");
            thirdInvocations.incrementAndGet();
            return null;
        };

        var service = new MortgageRuleService(List.of(first, second, third));
        return service;
    }

    @Test
    void shouldThrowBadRequest_whenHomeValueIsNull() {
        MortgageRule only = (income, loan, home) -> "Home value is required";
        var service = new MortgageRuleService(List.of(only));

        var ex = assertThrows(BadRequestException.class, () ->
                service.requestedLoanValidation(new BigDecimal("1000"), new BigDecimal("5000"), null));
        assertEquals("Home value is required", ex.getMessage());
    }

    @Test
    void shouldThrowBadRequest_whenIncomeIsNull() {
        MortgageRule rejectNullIncome = (income, loan, home) -> income == null ? "Income is required" : null;
        var service = new MortgageRuleService(List.of(rejectNullIncome));

        var ex = assertThrows(BadRequestException.class, () -> service.requestedLoanValidation(null, BigDecimal.ONE, BigDecimal.TEN));
        assertEquals("Income is required", ex.getMessage());
    }

    @Test
    void ShouldNotThrowAnyErrors_whenAllRulesPass() {
        var calls = new ArrayList<String>();
        MortgageRule r1 = (i, l, h) -> { calls.add("r1"); return null; };
        MortgageRule r2 = (i, l, h) -> { calls.add("r2"); return null; };
        MortgageRule r3 = (i, l, h) -> { calls.add("r3"); return null; };

        var service = new MortgageRuleService(List.of(r1, r2, r3));

        assertDoesNotThrow(() -> service.requestedLoanValidation(BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ONE));
        assertEquals(3, calls.size());
        assertEquals("r1", calls.getFirst());
        assertEquals("r3", calls.getLast());
    }
}
