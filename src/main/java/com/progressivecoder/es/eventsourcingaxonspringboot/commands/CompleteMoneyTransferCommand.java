package com.progressivecoder.es.eventsourcingaxonspringboot.commands;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class CompleteMoneyTransferCommand {
    @TargetAggregateIdentifier
    private String bankTransferId;
}
