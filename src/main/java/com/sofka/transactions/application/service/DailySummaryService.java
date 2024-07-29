package com.sofka.transactions.application.service;

import com.sofka.transactions.domain.model.DailySummary;
import com.sofka.transactions.domain.model.Transaction;
import com.sofka.transactions.domain.repository.DailySummaryRepository;
import com.sofka.transactions.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@RequiredArgsConstructor
@Service
public class DailySummaryService {

    private final TransactionRepository transactionRepository;
    private final DailySummaryRepository dailySummaryRepository;

    @Scheduled(cron = "${schedule.cron}")
    public Mono<Void> calculateDailySummary() {
        Instant startOdDay = LocalDate
                .now()
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC);

        Instant endOfDay = LocalDate
                .now()
                .plusDays(1)
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC);

        return transactionRepository
                .findAllByTimestampBetween(startOdDay, endOfDay)
                .collectList()
                .flatMap(transactions -> {
                    var totalAmount = transactions.stream().mapToDouble(Transaction::getAmount).sum();
                    var dailySummary = new DailySummary();
                    dailySummary.setDate(startOdDay);
                    dailySummary.setTotalAmount(totalAmount);
                    return dailySummaryRepository.save(dailySummary).then();
                });
    }

}
