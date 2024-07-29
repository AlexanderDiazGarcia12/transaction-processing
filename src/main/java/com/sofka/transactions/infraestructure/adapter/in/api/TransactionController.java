package com.sofka.transactions.infraestructure.adapter.in.api;

import com.sofka.transactions.application.port.in.CreateTransactionUseCase;
import com.sofka.transactions.domain.model.Transaction;
import com.sofka.transactions.infraestructure.adapter.out.messaging.RabbitMQTransactionMessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final CreateTransactionUseCase createTransactionUseCase;
    private final RabbitMQTransactionMessageSender rabbitMQTransactionMessageSender;

    @PostMapping
    public Mono<ResponseEntity<Transaction>> createTransaction(@RequestBody Transaction transaction) {
        return createTransactionUseCase
                .createTransaction(transaction)
                .flatMap(
                        savedTransaction -> {
                            rabbitMQTransactionMessageSender.save(savedTransaction);
                            return Mono.just(ResponseEntity.ok(savedTransaction));
                        }
                );

    }
}
