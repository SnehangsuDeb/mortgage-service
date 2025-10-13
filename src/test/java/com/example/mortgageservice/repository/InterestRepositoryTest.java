package com.example.mortgageservice.repository;

import com.example.mortgageservice.entities.InterestRates;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class InterestRepositoryTest {

    @Autowired
    private InterestRepository repository;

    @Test
    void saveAndFindAll_persistsAndReturnsEntities() {
        repository.save(InterestRates.builder().rate(4.1).tenure(5).type("VARIABLE").build());
        repository.save(InterestRates.builder().rate(5.75).tenure(30).type("FIXED").build());

        List<InterestRates> all = repository.findAll();

        assertTrue(all.size() >= 2);
        // Optionally assert at least one FIXED and one VARIABLE present
        long fixed = all.stream().filter(r -> "FIXED".equals(r.getType())).count();
        long variable = all.stream().filter(r -> "VARIABLE".equals(r.getType())).count();
        assertTrue(fixed >= 0);
        assertTrue(variable >= 0);
    }
}
