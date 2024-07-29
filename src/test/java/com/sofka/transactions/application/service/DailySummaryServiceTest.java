package com.sofka.transactions.application.service;

import com.sofka.transactions.domain.model.DailySummary;
import com.sofka.transactions.domain.model.Transaction;
import com.sofka.transactions.domain.repository.DailySummaryRepository;
import com.sofka.transactions.domain.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DailySummaryServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private DailySummaryRepository dailySummaryRepository;

    @InjectMocks
    private DailySummaryService dailySummaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateDailySummaryShouldCalculateDailySummaryCorrectly() {
        Instant startOfDay = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endOfDay = LocalDate.now().plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        List<Transaction> transactions = List.of(new Transaction("1", startOfDay.plusSeconds(3600), "device1", "user1", "geo1", 100.0), new Transaction("2", startOfDay.plusSeconds(7200), "device2", "user2", "geo2", 200.0));

        when(transactionRepository.findAllByTimestampBetween(startOfDay, endOfDay)).thenReturn(Flux.fromIterable(transactions));
        when(dailySummaryRepository.save(any(DailySummary.class))).thenReturn(Mono.just(new DailySummary()));

        Mono<Void> result = dailySummaryService.calculateDailySummary();

        StepVerifier.create(result).verifyComplete();

        verify(transactionRepository).findAllByTimestampBetween(startOfDay, endOfDay);
        verify(dailySummaryRepository).save(argThat(dailySummary -> dailySummary.getTotalAmount() == 300.0 && dailySummary.getDate().equals(startOfDay)));
    }

    @Test
    void testCalculateDailySummaryShouldRetrieveTransactionsWithinTimeRange() {
        Instant startOfDay = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endOfDay = LocalDate.now().plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        List<Transaction> transactions = List.of(new Transaction("1", startOfDay.plusSeconds(3600), "device1", "user1", "geo1", 100.0), new Transaction("2", startOfDay.plusSeconds(7200), "device2", "user2", "geo2", 200.0));

        when(transactionRepository.findAllByTimestampBetween(startOfDay, endOfDay)).thenReturn(Flux.fromIterable(transactions));
        when(dailySummaryRepository.save(any(DailySummary.class))).thenReturn(Mono.just(new DailySummary()));

        Mono<Void> result = dailySummaryService.calculateDailySummary();

        StepVerifier.create(result).verifyComplete();

        verify(transactionRepository).findAllByTimestampBetween(startOfDay, endOfDay);
    }

    @Test
    void testCalculateDailySummaryShouldHandleNoTransactionsFoundWithinTimeRange() {
        Instant startOfDay = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endOfDay = LocalDate.now().plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        when(transactionRepository.findAllByTimestampBetween(startOfDay, endOfDay)).thenReturn(Flux.empty());
        when(dailySummaryRepository.save(any(DailySummary.class))).thenReturn(Mono.just(new DailySummary()));

        Mono<Void> result = dailySummaryService.calculateDailySummary();

        StepVerifier.create(result).verifyComplete();

        verify(transactionRepository).findAllByTimestampBetween(startOfDay, endOfDay);
        verify(dailySummaryRepository).save(argThat(dailySummary -> dailySummary.getTotalAmount() == 0.0 && dailySummary.getDate().equals(startOfDay)));
    }

    @Test
    void testCalculateDailySummaryShouldHandleTransactionRepositoryError() {
        Instant startOfDay = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endOfDay = LocalDate.now().plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        when(transactionRepository.findAllByTimestampBetween(startOfDay, endOfDay)).thenReturn(Flux.error(new RuntimeException("Database error")));

        Mono<Void> result = dailySummaryService.calculateDailySummary();

        StepVerifier.create(result).expectError(RuntimeException.class).verify();

        verify(transactionRepository).findAllByTimestampBetween(startOfDay, endOfDay);
    }
}
