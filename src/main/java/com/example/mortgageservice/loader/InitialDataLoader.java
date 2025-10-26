package com.example.mortgageservice.loader;

import com.example.mortgageservice.entities.MortgageRates;
import com.example.mortgageservice.repository.MortgageRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitialDataLoader implements CommandLineRunner {
    private final MortgageRateRepository mortgageRateRepository;

    @Override
    public void run(String... args) {
        if (mortgageRateRepository.count() > 0) {
            log.info("Skipping seeding: MortgageRates already present.");
            return;
        }
        var count = mortgageRateRepository.saveAll(List.of(
                MortgageRates.builder().rate(3.9).mortgagePeriod(10).type("FIXED").build(),
                MortgageRates.builder().rate(5.75).mortgagePeriod(15).type("FIXED").build(),
                MortgageRates.builder().rate(7.5).mortgagePeriod(20).type("VARIABLE").build(),
                MortgageRates.builder().rate(8.5).mortgagePeriod(5).type("VARIABLE").build(),
                MortgageRates.builder().rate(6.5).mortgagePeriod(30).type("VARIABLE").build())).size();
        log.info("Seeding {} MortgageRates", count);
    }
}
