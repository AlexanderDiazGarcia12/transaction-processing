package com.sofka.transactions.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "dailySummaries")
public class DailySummary {
    @Id
    private String id;
    private Instant date;
    private double totalAmount;
}

