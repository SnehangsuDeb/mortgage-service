package com.example.mortgageservice.repository;


import com.example.mortgageservice.entities.MortgageRates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MortgageRateRepository extends JpaRepository<MortgageRates, Long> {
    MortgageRates getRateByMortgagePeriod(Integer maturityPeriod);
}
