package com.progressivecoder.es.eventsourcingaxonspringboot.aggregates;

import com.progressivecoder.es.eventsourcingaxonspringboot.commands.CancelMoneyTransferCommand;
import com.progressivecoder.es.eventsourcingaxonspringboot.commands.CompleteMoneyTransferCommand;
import com.progressivecoder.es.eventsourcingaxonspringboot.commands.RequestMoneyTransferCommand;
import com.progressivecoder.es.eventsourcingaxonspringboot.events.MoneyTransferCancelledEvent;
import com.progressivecoder.es.eventsourcingaxonspringboot.events.MoneyTransferCompletedEvent;
import com.progressivecoder.es.eventsourcingaxonspringboot.events.MoneyTransferRequestedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;
import static org.axonframework.modelling.command.AggregateLifecycle.markDeleted;

@NoArgsConstructor
@Aggregate
public class MoneyTransferAggregate {

    @AggregateIdentifier
    private String transactionId;

    @CommandHandler
    public MoneyTransferAggregate(RequestMoneyTransferCommand cmd) {
        apply(new MoneyTransferRequestedEvent(
                cmd.getTransactionId(),
                cmd.getSourceAccount(),
                cmd.getTargetAccount(),
                cmd.getAmount()));
    }

    @CommandHandler
    public void handle(CompleteMoneyTransferCommand cmd) {
        apply(new MoneyTransferCompletedEvent(transactionId));
    }

    @CommandHandler
    public void handle(CancelMoneyTransferCommand cmd) {
        apply(new MoneyTransferCancelledEvent(transactionId));
    }

    @EventSourcingHandler
    protected void on(MoneyTransferRequestedEvent event) {
        this.transactionId = event.getTransactionId();
    }

    @EventSourcingHandler
    protected void on(MoneyTransferCompletedEvent event) {
        markDeleted();
    }
}

