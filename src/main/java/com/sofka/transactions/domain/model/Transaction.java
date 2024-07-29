package com.sofka.transactions.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "transactions")
public class Transaction {
    @Id
    private String transactionId;
    private Instant timestamp;
    private String deviceNumber;
    private String userId;
    private String geoPosition;
    private double amount;
}
