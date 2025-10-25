package com.example.mortgageservice.mapper;

import com.example.mortgageservice.exceptions.NoContentException;
import com.example.mortgageservice.model.InterestTypeEnum;
import com.example.mortgageservice.model.MortgageRate;
import com.example.mortgageservice.model.MortgageRatesResponse;
import com.example.mortgageservice.entities.MortgageRates;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MortgageMapperTest {

    private final MortgageMapper mapper = new MortgageMapper();

    @Test
    void mapInterestRates_returnsNoContent_whenListIsEmpty() {
        assertThrows(NoContentException.class, () -> mapper.mapMortgageRates(List.of()));
    }

    @Test
    void mapInterestRates_mapsAllFieldsCorrectly() {
        List<MortgageRates> stored = List.of(
                MortgageRates.builder().rate(3.9).mortgagePeriod(10).type("FIXED").build(),
                MortgageRates.builder().rate(5.75).mortgagePeriod(15).type("VARIABLE").build()
        );

        MortgageRatesResponse response = mapper.mapMortgageRates(stored);

        assertNotNull(response);
        assertNotNull(response.mortgageRates());
        assertEquals(2, response.mortgageRates().size());

        MortgageRate first = response.mortgageRates().get(0);
        assertEquals(3.9, first.rate());
        assertEquals(10, first.tenure());
        assertEquals(InterestTypeEnum.FIXED, first.interestType());

        MortgageRate second = response.mortgageRates().get(1);
        assertEquals(5.75, second.rate());
        assertEquals(15, second.tenure());
        assertEquals(InterestTypeEnum.VARIABLE, second.interestType());
    }
}
