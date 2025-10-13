package com.example.mortgageservice.mapper;

import com.example.mortgageservice.controller.dto.InterestRate;
import com.example.mortgageservice.controller.dto.InterestRatesResponse;
import com.example.mortgageservice.entities.InterestRates;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MortgageMapperTest {

    private final MortgageMapper mapper = new MortgageMapper();

    @Test
    void mapInterestRates_returnsEmptyResponse_whenListIsNull() {
        InterestRatesResponse response = mapper.mapInterestRates(null);
        assertNotNull(response);
        assertNotNull(response.getInterestRates());
        assertTrue(response.getInterestRates().isEmpty());
    }

    @Test
    void mapInterestRates_returnsEmptyResponse_whenListIsEmpty() {
        InterestRatesResponse response = mapper.mapInterestRates(List.of());
        assertNotNull(response);
        assertNotNull(response.getInterestRates());
        assertTrue(response.getInterestRates().isEmpty());
    }

    @Test
    void mapInterestRates_mapsAllFieldsCorrectly() {
        List<InterestRates> stored = List.of(
                InterestRates.builder().rate(3.9).tenure(10).type("FIXED").build(),
                InterestRates.builder().rate(5.75).tenure(15).type("VARIABLE").build()
        );

        InterestRatesResponse response = mapper.mapInterestRates(stored);

        assertNotNull(response);
        assertNotNull(response.getInterestRates());
        assertEquals(2, response.getInterestRates().size());

        InterestRate first = response.getInterestRates().get(0);
        assertEquals(3.9, first.getRate());
        assertEquals(10, first.getTenure());
        assertEquals(InterestRate.InterestTypeEnum.FIXED, first.getInterestType());

        InterestRate second = response.getInterestRates().get(1);
        assertEquals(5.75, second.getRate());
        assertEquals(15, second.getTenure());
        assertEquals(InterestRate.InterestTypeEnum.VARIABLE, second.getInterestType());
    }
}
