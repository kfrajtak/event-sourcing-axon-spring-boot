package com.progressivecoder.es.eventsourcingaxonspringboot.sagas;

import com.progressivecoder.es.eventsourcingaxonspringboot.commands.CancelMoneyTransferCommand;
import com.progressivecoder.es.eventsourcingaxonspringboot.commands.CompleteMoneyTransferCommand;
import com.progressivecoder.es.eventsourcingaxonspringboot.commands.DepositMoneyCommand;
import com.progressivecoder.es.eventsourcingaxonspringboot.commands.WithdrawMoneyCommand;
import com.progressivecoder.es.eventsourcingaxonspringboot.events.*;
import org.axonframework.commandhandling.callbacks.LoggingCallback;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import static org.axonframework.modelling.saga.SagaLifecycle.end;

@Saga
public class MoneyTransferSaga {
    @Autowired
    private transient CommandGateway commandGateway;

    private String targetAccountId;
    private String transactionId;

    @StartSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void on(MoneyTransferRequestedEvent event) {
        // generated identifiers
        targetAccountId = event.getTargetBankAccountId();
        transactionId = event.getTransactionId();
        // associate the Saga with these values, before sending the commands (see SagaEventHandler below)
        // transactionId is enough for saga identifier
        SagaLifecycle.associateWith("transactionId", transactionId);
        commandGateway.send(new WithdrawMoneyCommand(event.getSourceAccountId(), transactionId, event.getAmount()),
                (commandMessage, commandResultMessage) -> {
                    // money transfer has failed
                    if (!commandResultMessage.isExceptional()) {
                        return;
                    }

                    commandGateway.send(new CancelMoneyTransferCommand(
                            event.getTransactionId(),
                            commandResultMessage.optionalExceptionResult().get().getMessage()));
                });
    }

    @SagaEventHandler(associationProperty = "transactionId")
    public void on(MoneyWithdrawnEvent event) {
        commandGateway.send(new DepositMoneyCommand(targetAccountId, event.getTransactionId(), event.getAmount()),
                LoggingCallback.INSTANCE);
    }

    @SagaEventHandler(associationProperty = "transactionId")
    public void on(MoneyDepositedEvent event) {
        commandGateway.send(new CompleteMoneyTransferCommand(transactionId),
                LoggingCallback.INSTANCE);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "transactionId")
    public void on(MoneyTransferCompletedEvent event) {
    }

    @SagaEventHandler(associationProperty = "transactionId")
    public void on(MoneyTransferCancelledEvent event) {
        end();
    }
}
