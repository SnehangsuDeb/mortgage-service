package com.example.mortgageservice.loader;

import com.example.mortgageservice.entities.MortgageRates;
import com.example.mortgageservice.repository.MortgageRateRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class InitialDataLoaderTest {

    @Test
    void run_insertsThreeRecords() throws Exception {
        MortgageRateRepository repo = mock(MortgageRateRepository.class);
        InitialDataLoader loader = new InitialDataLoader(repo);

        loader.run();

        ArgumentCaptor<MortgageRates> captor = ArgumentCaptor.forClass(MortgageRates.class);
        verify(repo, times(3)).save(captor.capture());
        assertEquals(3, captor.getAllValues().size());
        MortgageRates first = captor.getAllValues().get(0);
    }
}
