package com.example.mortgageservice.repository;

import com.example.mortgageservice.entities.MortgageRates;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MortgageRateRepositoryTest {

    @Autowired
    private MortgageRateRepository repository;

    @Test
    void saveAndFindAll_persistsAndFindsAllEntities() {
        repository.save(MortgageRates.builder().rate(4.1).mortgagePeriod(5).type("VARIABLE").build());
        repository.save(MortgageRates.builder().rate(5.75).mortgagePeriod(30).type("FIXED").build());

        List<MortgageRates> all = repository.findAll();

        assertEquals(2, all.size(), "Exactly two rows should be persisted in an empty test database");

        MortgageRates var = all.stream().filter(r -> "VARIABLE".equals(r.getType())).findFirst().orElseThrow();
        assertEquals(4.1, var.getRate());
        assertEquals(5, var.getMortgagePeriod());

        MortgageRates fix = all.stream().filter(r -> "FIXED".equals(r.getType())).findFirst().orElseThrow();
        assertEquals(5.75, fix.getRate());
        assertEquals(30, fix.getMortgagePeriod());
    }

    @Test
    void getRateByMortgagePeriod_returnsEntity_whenExists() {
        repository.save(MortgageRates.builder().rate(4.1).mortgagePeriod(5).type("VARIABLE").build());
        repository.save(MortgageRates.builder().rate(5.75).mortgagePeriod(30).type("FIXED").build());
        MortgageRates response = repository.getRateByMortgagePeriod(30);
        assertNotNull(response, "Expected an entity for mortgagePeriod 30");
        assertEquals(30, response.getMortgagePeriod());
        assertEquals(5.75, response.getRate());
        assertEquals("FIXED", response.getType());
    }

    @Test
    void getRateByMortgagePeriod_returnsNull_whenAbsent() {
        MortgageRates response = repository.getRateByMortgagePeriod(99);
        assertNull(response);
    }
}
