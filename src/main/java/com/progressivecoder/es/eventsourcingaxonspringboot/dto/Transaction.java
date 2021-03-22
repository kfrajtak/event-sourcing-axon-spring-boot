package com.progressivecoder.es.eventsourcingaxonspringboot.dto;

import lombok.Value;

@Value
public class Transaction {
    private String transactionId;
    private String sourceAccount;
    private String targetAccount;
    private double amount;
}
