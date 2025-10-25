package com.example.mortgageservice.loader;

import com.example.mortgageservice.entities.MortgageRates;
import com.example.mortgageservice.repository.MortgageRateRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class InitialDataLoaderTest {

    @Test
    void run_insertsRecords_whenRepositoryEmpty() throws Exception {
        MortgageRateRepository repo = mock(MortgageRateRepository.class);
        when(repo.count()).thenReturn(0L);
        when(repo.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        InitialDataLoader loader = new InitialDataLoader(repo);

        loader.run();
        ArgumentCaptor<List<MortgageRates>> captor = ArgumentCaptor.forClass((Class) List.class);
        verify(repo, times(1)).saveAll(captor.capture());
        assertEquals(5, captor.getValue().size());
    }

    @Test
    void run_skipsSeeding_whenRepositoryHasData() throws Exception {
        MortgageRateRepository repo = mock(MortgageRateRepository.class);
        when(repo.count()).thenReturn(1L);

        InitialDataLoader loader = new InitialDataLoader(repo);

        loader.run();

        verify(repo, never()).saveAll(anyList());
    }
}
