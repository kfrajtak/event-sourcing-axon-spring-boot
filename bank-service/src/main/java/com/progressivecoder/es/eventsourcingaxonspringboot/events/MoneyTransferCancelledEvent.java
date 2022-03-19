package com.progressivecoder.es.eventsourcingaxonspringboot.events;

import lombok.Value;

@Value
public class MoneyTransferCancelledEvent {
    String transactionId;
}
