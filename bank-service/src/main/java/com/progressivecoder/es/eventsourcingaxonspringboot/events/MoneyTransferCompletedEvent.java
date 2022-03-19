package com.progressivecoder.es.eventsourcingaxonspringboot.events;

import lombok.Value;

@Value
public class MoneyTransferCompletedEvent {
    String transactionId;
}
