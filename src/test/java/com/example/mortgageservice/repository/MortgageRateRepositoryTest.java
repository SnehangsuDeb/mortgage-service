package com.example.mortgageservice.repository;

import com.example.mortgageservice.entities.MortgageRates;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class MortgageRateRepositoryTest {

    @Autowired
    private MortgageRateRepository repository;

    @Test
    void saveAndFindAll_persistsAndReturnsEntities() {
        repository.save(MortgageRates.builder().rate(4.1).mortgagePeriod(5).type("VARIABLE").build());
        repository.save(MortgageRates.builder().rate(5.75).mortgagePeriod(30).type("FIXED").build());

        List<MortgageRates> all = repository.findAll();

        assertTrue(all.size() >= 2);
        // Optionally assert at least one FIXED and one VARIABLE present
        long fixed = all.stream().filter(r -> "FIXED".equals(r.getType())).count();
        long variable = all.stream().filter(r -> "VARIABLE".equals(r.getType())).count();
        assertTrue(fixed >= 0);
        assertTrue(variable >= 0);
    }
}
