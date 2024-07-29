package com.sofka.transactions.domain.repository;

import com.sofka.transactions.domain.model.DailySummary;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Repository
public interface DailySummaryRepository extends ReactiveMongoRepository<DailySummary, String> {
    Mono<DailySummary> findByDate(Instant date);
}
