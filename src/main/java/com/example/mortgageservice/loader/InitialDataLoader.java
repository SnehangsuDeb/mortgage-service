package com.example.mortgageservice.loader;

import com.example.mortgageservice.entities.MortgageRates;
import com.example.mortgageservice.repository.MortgageRateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InitialDataLoader implements CommandLineRunner {
    private final MortgageRateRepository mortgageRateRepository;
    public InitialDataLoader(MortgageRateRepository mortgageRateRepository) {
        this.mortgageRateRepository = mortgageRateRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        mortgageRateRepository.save(MortgageRates.builder().rate(3.9).mortgagePeriod(10).type("FIXED").build());
        mortgageRateRepository.save(MortgageRates.builder().rate(5.75).mortgagePeriod(15).type("FIXED").build());
        mortgageRateRepository.save(MortgageRates.builder().rate(7.5).mortgagePeriod(20).type("VARIABLE").build());
        mortgageRateRepository.save(MortgageRates.builder().rate(8.5).mortgagePeriod(5).type("VARIABLE").build());
        log.info("******************Loaded initial data********************");
    }
}
