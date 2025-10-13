package com.example.mortgageservice.mapper;

import com.example.mortgageservice.controller.dto.InterestRate;
import com.example.mortgageservice.controller.dto.InterestRatesResponse;
import com.example.mortgageservice.entities.InterestRates;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class MortgageMapper {
    public InterestRatesResponse mapInterestRates(List<InterestRates> storedInterestRates) {
        InterestRatesResponse response = new InterestRatesResponse();
        if (storedInterestRates == null || storedInterestRates.isEmpty()) {
            return response;
        }

        List<InterestRate> rates = storedInterestRates.stream()
                .filter(Objects::nonNull)
                .map(entity -> new InterestRate(
                        entity.getRate(),
                        entity.getTenure(),
                        entity.getType() == null ? null : InterestRate.InterestTypeEnum.fromValue(entity.getType())
                ))
                .collect(Collectors.toList());

        response.setInterestRates(rates);
        return response;
    }
}
