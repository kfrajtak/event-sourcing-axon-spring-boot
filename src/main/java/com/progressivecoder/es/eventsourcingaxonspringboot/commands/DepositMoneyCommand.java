package com.progressivecoder.es.eventsourcingaxonspringboot.commands;

import lombok.Getter;
import org.axonframework.commandhandling.RoutingKey;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.lang.annotation.Annotation;

/*public class DepositMoneyCommand {
    @TargetAggregateIdentifier
    @Getter
    private final String accountId;

    public final double amount;

    public final String transactionId;

    public DepositMoneyCommand(String accountId, String transactionId, double amount) {
        this.accountId = accountId;
        this.amount = amount;
        this.transactionId = transactionId;
    }
}
*/
public class DepositMoneyCommand {

    @TargetAggregateIdentifier
    public final String transactionId;

    public final double amount;

    public final String accountId;

    public DepositMoneyCommand(String accountId, String transactionId, double amount) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.amount = amount;
    }
}