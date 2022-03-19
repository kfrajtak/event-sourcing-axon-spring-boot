package com.progressivecoder.es.eventsourcingaxonspringboot.commands;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class DepositMoneyCommand {
    @TargetAggregateIdentifier
    String targetAccountId;
    String transactionId;
    double amount;
}