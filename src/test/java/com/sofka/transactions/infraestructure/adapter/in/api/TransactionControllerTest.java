package com.sofka.transactions.infraestructure.adapter.in.api;

import com.sofka.transactions.application.port.in.CreateTransactionUseCase;
import com.sofka.transactions.domain.model.Transaction;
import com.sofka.transactions.infraestructure.adapter.out.messaging.RabbitMQTransactionMessageSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransactionControllerTest {

    @Mock
    private CreateTransactionUseCase createTransactionUseCase;

    @Mock
    private RabbitMQTransactionMessageSender rabbitMQTransactionMessageSender;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveShouldCreateTransactionSuccessfully() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId("123");
        transaction.setTimestamp(Instant.now());
        transaction.setDeviceNumber("device123");
        transaction.setUserId("user123");
        transaction.setGeoPosition("geo123");
        transaction.setAmount(100.0);

        when(createTransactionUseCase.createTransaction(any(Transaction.class))).thenReturn(Mono.just(transaction));
        when(rabbitMQTransactionMessageSender.save(any(Transaction.class))).thenReturn(Mono.just(transaction));

        Mono<ResponseEntity<Transaction>> response = transactionController.createTransaction(transaction);

        StepVerifier.create(response).expectNextMatches(res -> res.getStatusCode().equals(HttpStatus.OK) && res.getBody().equals(transaction)).verifyComplete();

        verify(createTransactionUseCase).createTransaction(any(Transaction.class));
        verify(rabbitMQTransactionMessageSender).save(any(Transaction.class));
    }
}
