package com.example.mortgageservice.mapper;

import com.example.mortgageservice.model.MortgageRate;
import com.example.mortgageservice.model.MortgageRatesResponse;
import com.example.mortgageservice.entities.MortgageRates;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MortgageMapperTest {

    private final MortgageMapper mapper = new MortgageMapper();

    @Test
    void mapInterestRates_returnsEmptyResponse_whenListIsNull() {
        MortgageRatesResponse response = mapper.mapMortgageRates(null);
        assertNotNull(response);
        assertNotNull(response.getMortgageRates());
        assertTrue(response.getMortgageRates().isEmpty());
    }

    @Test
    void mapInterestRates_returnsEmptyResponse_whenListIsEmpty() {
        MortgageRatesResponse response = mapper.mapMortgageRates(List.of());
        assertNotNull(response);
        assertNotNull(response.getMortgageRates());
        assertTrue(response.getMortgageRates().isEmpty());
    }

    @Test
    void mapInterestRates_mapsAllFieldsCorrectly() {
        List<MortgageRates> stored = List.of(
                MortgageRates.builder().rate(3.9).mortgagePeriod(10).type("FIXED").build(),
                MortgageRates.builder().rate(5.75).mortgagePeriod(15).type("VARIABLE").build()
        );

        MortgageRatesResponse response = mapper.mapMortgageRates(stored);

        assertNotNull(response);
        assertNotNull(response.getMortgageRates());
        assertEquals(2, response.getMortgageRates().size());

        MortgageRate first = response.getMortgageRates().get(0);
        assertEquals(3.9, first.getRate());
        assertEquals(10, first.getTenure());
        assertEquals(MortgageRate.InterestTypeEnum.FIXED, first.getInterestType());

        MortgageRate second = response.getMortgageRates().get(1);
        assertEquals(5.75, second.getRate());
        assertEquals(15, second.getTenure());
        assertEquals(MortgageRate.InterestTypeEnum.VARIABLE, second.getInterestType());
    }
}
