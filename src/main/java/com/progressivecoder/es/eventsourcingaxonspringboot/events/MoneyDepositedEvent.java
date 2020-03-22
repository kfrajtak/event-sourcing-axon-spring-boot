package com.progressivecoder.es.eventsourcingaxonspringboot.events;

import lombok.Value;

@Value
public class MoneyDepositedEvent {
    String transactionId;
    String accountId;
    double balance;
    double amount;
}
