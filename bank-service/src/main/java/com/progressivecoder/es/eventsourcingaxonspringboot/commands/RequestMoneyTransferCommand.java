package com.progressivecoder.es.eventsourcingaxonspringboot.commands;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class RequestMoneyTransferCommand {
    @TargetAggregateIdentifier
    String transactionId;
    String sourceAccount;
    String targetAccount;
    double amount;
}
