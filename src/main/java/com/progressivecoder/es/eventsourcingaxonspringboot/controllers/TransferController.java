package com.progressivecoder.es.eventsourcingaxonspringboot.controllers;

import com.progressivecoder.es.eventsourcingaxonspringboot.commands.CreateAccountCommand;
import com.progressivecoder.es.eventsourcingaxonspringboot.commands.RequestMoneyTransferCommand;
import io.swagger.annotations.Api;
import org.axonframework.commandhandling.callbacks.LoggingCallback;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@Api(value = "Transfer commands")
public class TransferController {

    @Autowired
    private CommandGateway commandGateway;

    @GetMapping("/init")
    public String sendMessage() {

        CreateAccountCommand createSourceAccountCommand = new CreateAccountCommand(800, "CZK", 0);
        CreateAccountCommand createTargetAccountCommand = new CreateAccountCommand(1200, "CZK", 0);

        commandGateway.send(createSourceAccountCommand, LoggingCallback.INSTANCE);
        commandGateway.send(createTargetAccountCommand, LoggingCallback.INSTANCE);
        commandGateway.send(new RequestMoneyTransferCommand(
                "tf1",
                createSourceAccountCommand.getAccountId(),
                createTargetAccountCommand.getAccountId(),
                200), LoggingCallback.INSTANCE);

        return "OK";
    }

    @GetMapping("/transfer/{sourceAccount}/{destinationAccount}/{amount}")
    public CompletableFuture<Object> transfer(@PathVariable String sourceAccount, @PathVariable String destinationAccount, @PathVariable double amount) {
        String transactionId = UUID.randomUUID().toString();
        return commandGateway.send(new RequestMoneyTransferCommand(transactionId, sourceAccount, destinationAccount, amount))
                .exceptionally(e -> e);
    }
}
