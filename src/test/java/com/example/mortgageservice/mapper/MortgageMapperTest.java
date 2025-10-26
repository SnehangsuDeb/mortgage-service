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
    void shouldReturnNoContent_whenListIsEmpty() {
        assertThrows(NoContentException.class, () -> mapper.mapMortgageRates(List.of()));
    }

    @Test
    void shouldReturnNoContent_whenListIsNull() {
        assertThrows(NoContentException.class, () -> mapper.mapMortgageRates(null));
    }

    @Test
    void shouldReturnDataInOrderOfInsertion() {
        var stored = List.of(
                MortgageRates.builder().rate(3.9).mortgagePeriod(10).type("FIXED").build(),
                MortgageRates.builder().rate(5.75).mortgagePeriod(15).type("VARIABLE").build()
        );

        var response = mapper.mapMortgageRates(stored);

        assertNotNull(response);
        assertNotNull(response.mortgageRates());
        assertEquals(2, response.mortgageRates().size());

        var first = response.mortgageRates().getFirst();
        assertEquals(3.9, first.rate());
        assertEquals(10, first.tenure());
        assertEquals(InterestTypeEnum.FIXED, first.interestType());

        var last = response.mortgageRates().getLast();
        assertEquals(5.75, last.rate());
        assertEquals(15, last.tenure());
        assertEquals(InterestTypeEnum.VARIABLE, last.interestType());
    }

    @Test
    void shouldReturnData_forFirstAndLastInterestRateAndTenure() {
        var stored = List.of(
                MortgageRates.builder().rate(1.1).mortgagePeriod(5).type("FIXED").build(),
                MortgageRates.builder().rate(2.2).mortgagePeriod(10).type("VARIABLE").build(),
                MortgageRates.builder().rate(3.3).mortgagePeriod(15).type("FIXED").build()
        );

        var response = mapper.mapMortgageRates(stored);

        var first = response.mortgageRates().getFirst();
        var last = response.mortgageRates().getLast();

        assertEquals(1.1, first.rate());
        assertEquals(5, first.tenure());

        assertEquals(3.3, last.rate());
        assertEquals(15, last.tenure());
    }

    @Test
    void shouldReturnResult_byMappingUnknownTypeToNull() {
        var stored = List.of(
                MortgageRates.builder().rate(4.2).mortgagePeriod(25).type("UNKNOWN").build()
        );

        var response = mapper.mapMortgageRates(stored);

        assertNotNull(response);
        assertEquals(1, response.mortgageRates().size());
        var only = response.mortgageRates().getFirst();
        assertEquals(4.2, only.rate());
        assertEquals(25, only.tenure());
        assertNull(only.interestType(), "Unknown type should map to null interestType");
    }

    @Test
    void shouldReturnResult_byMappingNullTypeToNull() {
        var stored = List.of(
                MortgageRates.builder().rate(7.7).mortgagePeriod(40).type(null).build()
        );

        var response = mapper.mapMortgageRates(stored);

        assertEquals(1, response.mortgageRates().size());
        var only = response.mortgageRates().getFirst();
        assertEquals(7.7, only.rate());
        assertEquals(40, only.tenure());
        assertNull(only.interestType());
    }

    @Test
    void shouldThrowUnsupportedOperationException_whenRequestSentWithUnknownMortgagePeriod() {
        var stored = List.of(
                MortgageRates.builder().rate(2.5).mortgagePeriod(12).type("FIXED").build()
        );

        var response = mapper.mapMortgageRates(stored);

        assertThrows(UnsupportedOperationException.class, () ->
                response.mortgageRates().add(new MortgageRate(3.0, 10, InterestTypeEnum.VARIABLE)));
    }
}
