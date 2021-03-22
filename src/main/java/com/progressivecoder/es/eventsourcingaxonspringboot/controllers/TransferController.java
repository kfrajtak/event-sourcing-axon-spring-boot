package com.progressivecoder.es.eventsourcingaxonspringboot.controllers;

import com.progressivecoder.es.eventsourcingaxonspringboot.commands.CreateAccountCommand;
import com.progressivecoder.es.eventsourcingaxonspringboot.commands.RequestMoneyTransferCommand;
import com.progressivecoder.es.eventsourcingaxonspringboot.dto.Transaction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.axonframework.commandhandling.callbacks.LoggingCallback;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@Api(value = "Transfer commands", description = "Account Transfer Events Endpoint", tags = "Transfer commands")
public class TransferController {

    @Autowired
    private CommandGateway commandGateway;

    @GetMapping("/saga-showcase")
    @ApiOperation(value = "Creates two accounts and transfers money between them", tags = "Transfer commands")
    public Transaction sagaShowcase() {
        String transactionId = UUID.randomUUID().toString();

        CreateAccountCommand createSourceAccountCommand = new CreateAccountCommand(800, "CZK", 0);
        CreateAccountCommand createTargetAccountCommand = new CreateAccountCommand(1200, "CZK", 0);

        commandGateway.send(createSourceAccountCommand, LoggingCallback.INSTANCE);
        commandGateway.send(createTargetAccountCommand, LoggingCallback.INSTANCE);

        RequestMoneyTransferCommand command = new RequestMoneyTransferCommand(
                transactionId,
                createSourceAccountCommand.getAccountId(),
                createTargetAccountCommand.getAccountId(),
                200);

        commandGateway.send(command, LoggingCallback.INSTANCE);

        return new Transaction(command.getTransactionId(), command.getSourceAccount(), command.getTargetAccount(), command.getAmount());
    }

    @GetMapping("/transfer/{sourceAccount}/{destinationAccount}/{amount}")
    @ApiOperation(value = "Transfers money from source account to target account", tags = "Transfer commands")
    public CompletableFuture<Object> transfer(@PathVariable String sourceAccount, @PathVariable String destinationAccount, @PathVariable double amount) {
        String transactionId = UUID.randomUUID().toString();
        return commandGateway.send(new RequestMoneyTransferCommand(transactionId, sourceAccount, destinationAccount, amount))
                .exceptionally(e -> e);
    }
}
