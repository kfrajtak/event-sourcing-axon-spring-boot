package com.progressivecoder.es.eventsourcingaxonspringboot.commands;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Value
public class CreateAccountCommand {
    @TargetAggregateIdentifier
    private String accountId;
    private double accountBalance;
    private final String currency;
    private final double overdraftLimit;

    public CreateAccountCommand(double accountBalance, String currency, double overdraftLimit) {
        accountId = UUID.randomUUID().toString();
        this.accountBalance = accountBalance;
        this.currency = currency;
        this.overdraftLimit = overdraftLimit;
    }
}
