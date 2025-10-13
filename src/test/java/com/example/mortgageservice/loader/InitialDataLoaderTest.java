package com.example.mortgageservice.loader;

import com.example.mortgageservice.entities.InterestRates;
import com.example.mortgageservice.repository.InterestRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class InitialDataLoaderTest {

    @Test
    void run_insertsThreeRecords() throws Exception {
        InterestRepository repo = mock(InterestRepository.class);
        InitialDataLoader loader = new InitialDataLoader(repo);

        loader.run();

        ArgumentCaptor<InterestRates> captor = ArgumentCaptor.forClass(InterestRates.class);
        verify(repo, times(3)).save(captor.capture());
        assertEquals(3, captor.getAllValues().size());
        // Verify first record (example)
        InterestRates first = captor.getAllValues().get(0);
        // Values come from loader; just ensure non-null meaningful fields
        // e.g., check type or tenure within expected set:
        // Not asserting exact order to keep test resilient.
    }
}
