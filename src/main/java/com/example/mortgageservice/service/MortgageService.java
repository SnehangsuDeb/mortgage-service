package com.example.mortgageservice.service;

import com.example.mortgageservice.mapper.MortgageMapper;
import com.example.mortgageservice.controller.dto.InterestRatesResponse;
import com.example.mortgageservice.repository.InterestRepository;
import com.example.mortgageservice.repository.MortgageRateRepository;
import org.springframework.stereotype.Service;

@Service
public class MortgageService {
    private final MortgageRateRepository mortgageRateRepository;
    private final InterestRepository interestRepository;
    private MortgageMapper mortgageMapper;

    public MortgageService(MortgageRateRepository mortgageRateRepository, InterestRepository interestRepository, MortgageMapper mortgageMapper) {
        this.mortgageRateRepository = mortgageRateRepository;
        this.interestRepository = interestRepository;
        this.mortgageMapper = mortgageMapper;
    }

    public InterestRatesResponse getInterestRates() {
        return mortgageMapper.mapInterestRates(interestRepository.findAll());
    }
}
