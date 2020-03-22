package com.progressivecoder.es.eventsourcingaxonspringboot.commands;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
@AllArgsConstructor
public class DepositMoneyCommand {
    @TargetAggregateIdentifier
    String accountId;
    String transactionId;
    double amount;
}
