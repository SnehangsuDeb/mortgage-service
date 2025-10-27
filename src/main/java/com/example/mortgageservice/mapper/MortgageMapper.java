package com.example.mortgageservice.mapper;

import com.example.mortgageservice.model.InterestTypeEnum;
import com.example.mortgageservice.model.MortgageRate;
import com.example.mortgageservice.model.MortgageRatesResponse;
import com.example.mortgageservice.entities.MortgageRates;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class MortgageMapper {
    public MortgageRatesResponse mapMortgageRates(List<MortgageRates> storedMortgageRates) {
        List<MortgageRate> rates = storedMortgageRates.stream()
                .filter(Objects::nonNull)
                .map(entity -> {
                    var interestType = switch (entity.getType()) {
                        case "FIXED" -> InterestTypeEnum.FIXED;
                        case "VARIABLE" -> InterestTypeEnum.VARIABLE;
                        case null, default -> null;
                    };
                    return new MortgageRate(entity.getRate(), entity.getMortgagePeriod(), interestType);

                })
                .toList();
        return new MortgageRatesResponse(rates);
    }
}
