package com.progressivecoder.es.eventsourcingaxonspringboot.commands;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class WithdrawMoneyCommand {
    @TargetAggregateIdentifier
    String sourceAccountId;
    String transactionId;
    double amount;
}