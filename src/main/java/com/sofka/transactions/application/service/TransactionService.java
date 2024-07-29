package com.sofka.transactions.application.service;

import com.sofka.transactions.application.port.in.CreateTransactionUseCase;
import com.sofka.transactions.application.port.out.SaveTransactionPort;
import com.sofka.transactions.domain.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class TransactionService implements CreateTransactionUseCase {

    private final SaveTransactionPort saveTransactionPort;

    @Override
    public Mono<Transaction> createTransaction(Transaction transaction) {
        return saveTransactionPort.save(transaction);
    }
}
