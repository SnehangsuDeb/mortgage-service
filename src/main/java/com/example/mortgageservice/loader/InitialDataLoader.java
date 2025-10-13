package com.example.mortgageservice.loader;

import com.example.mortgageservice.entities.InterestRates;
import com.example.mortgageservice.repository.InterestRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoader implements CommandLineRunner {
    private final InterestRepository interestRepository;
    public InitialDataLoader(InterestRepository interestRepository) {
        this.interestRepository = interestRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        interestRepository.save(InterestRates.builder().rate(3.9).tenure(10).type("FIXED").build());
        interestRepository.save(InterestRates.builder().rate(5.75).tenure(15).type("FIXED").build());
        interestRepository.save(InterestRates.builder().rate(7.5).tenure(20).type("VARIABLE").build());
    }
}
