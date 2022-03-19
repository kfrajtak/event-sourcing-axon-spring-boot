package com.progressivecoder.es.eventsourcingaxonspringboot.events;

import lombok.Value;

@Value
public class MoneyTransferRequestedEvent {
    String transactionId;
    String sourceAccountId;
    String targetBankAccountId;
    double amount;
}
