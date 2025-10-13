package com.example.mortgageservice.mapper;

import com.example.mortgageservice.controller.dto.MortgageRate;
import com.example.mortgageservice.controller.dto.MortgageRatesResponse;
import com.example.mortgageservice.entities.MortgageRates;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class MortgageMapper {
    public MortgageRatesResponse mapMortgageRates(List<MortgageRates> storedMortgageRates) {
        MortgageRatesResponse response = new MortgageRatesResponse();
        if (storedMortgageRates == null || storedMortgageRates.isEmpty()) {
            return response;
        }

        List<MortgageRate> rates = storedMortgageRates.stream()
                .filter(Objects::nonNull)
                .map(entity -> new MortgageRate(
                        entity.getRate(),
                        entity.getMortgagePeriod(),
                        entity.getType() == null ? null : MortgageRate.InterestTypeEnum.fromValue(entity.getType())
                ))
                .collect(Collectors.toList());

        response.setMortgageRates(rates);
        return response;
    }
}
