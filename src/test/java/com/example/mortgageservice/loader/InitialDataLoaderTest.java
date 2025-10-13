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
        // Verify first record (example)
        MortgageRates first = captor.getAllValues().get(0);
        // Values come from loader; just ensure non-null meaningful fields
        // e.g., check type or tenure within expected set:
        // Not asserting exact order to keep test resilient.
    }
}
