package com.sofka.transactions.application.port.in;

import com.sofka.transactions.domain.model.Transaction;
import reactor.core.publisher.Mono;

public interface CreateTransactionUseCase {
    Mono<Transaction> createTransaction(Transaction transaction);
}
