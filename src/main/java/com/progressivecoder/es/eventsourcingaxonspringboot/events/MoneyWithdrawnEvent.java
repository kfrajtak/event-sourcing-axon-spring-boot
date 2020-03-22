package com.progressivecoder.es.eventsourcingaxonspringboot.events;

import lombok.Value;

@Value
public class MoneyWithdrawnEvent  {
    String id;
    String transactionId;
    double withDrawnAmount;
    double amount;
}
