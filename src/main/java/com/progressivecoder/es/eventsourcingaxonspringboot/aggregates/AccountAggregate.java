package com.progressivecoder.es.eventsourcingaxonspringboot.aggregates;

import com.progressivecoder.es.eventsourcingaxonspringboot.commands.*;
import com.progressivecoder.es.eventsourcingaxonspringboot.events.*;
import com.progressivecoder.es.eventsourcingaxonspringboot.exceptions.AccountHeldException;
import com.progressivecoder.es.eventsourcingaxonspringboot.exceptions.OverdraftLimitExceededException;
import lombok.Getter;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
@Getter
public class AccountAggregate {
    @AggregateIdentifier
    private String id;
    private double accountBalance;
    private double overdraftLimit;
    private String currency;
    private String status;

    public AccountAggregate() {
    }

    @CommandHandler
    public AccountAggregate(CreateAccountCommand createAccountCommand) {
        apply(new AccountCreatedEvent(createAccountCommand.getAccountId(), createAccountCommand.getAccountBalance(), createAccountCommand.getCurrency()));
    }

    @EventSourcingHandler
    protected void on(AccountCreatedEvent accountCreatedEvent) {
        this.id = accountCreatedEvent.id;
        this.accountBalance = accountCreatedEvent.accountBalance;
        this.currency = accountCreatedEvent.currency;
        this.status = String.valueOf(Status.CREATED);

        apply(new AccountActivatedEvent(this.id, Status.ACTIVATED));
    }

    @EventSourcingHandler
    protected void on(AccountActivatedEvent accountActivatedEvent) {
        this.status = String.valueOf(accountActivatedEvent.status);
    }

    @CommandHandler
    protected void on(CreditMoneyCommand creditMoneyCommand) {
        apply(new MoneyCreditedEvent(creditMoneyCommand.id, creditMoneyCommand.creditAmount, creditMoneyCommand.currency));
    }

    @CommandHandler
    protected void on(DepositMoneyCommand cmd) {
        apply(new MoneyDepositedEvent(cmd.getTransactionId(), cmd.getTargetAccountId(),accountBalance + cmd.getAmount(), cmd.getAmount()));
    }

    @EventSourcingHandler
    protected void on(MoneyDepositedEvent moneyDepositedEvent) {
        this.accountBalance += moneyDepositedEvent.getAmount();
    }

    @EventSourcingHandler
    protected void on(MoneyCreditedEvent moneyCreditedEvent) {
        if (this.accountBalance < 0 & (this.accountBalance + moneyCreditedEvent.creditAmount) >= 0) {
            apply(new AccountActivatedEvent(this.id, Status.ACTIVATED));
        }

        this.accountBalance += moneyCreditedEvent.creditAmount;
    }

    @CommandHandler
    protected void on(DebitMoneyCommand debitMoneyCommand) {
        if (accountBalance >= 0 & (accountBalance - debitMoneyCommand.debitAmount) < 0) {
            apply(new AccountHeldEvent(id, Status.HOLD));
            return;
        }

        accountBalance -= debitMoneyCommand.debitAmount;
        apply(new MoneyDebitedEvent(debitMoneyCommand.id, debitMoneyCommand.debitAmount, debitMoneyCommand.currency));
    }

    @CommandHandler
    public void on(WithdrawMoneyCommand cmd) throws OverdraftLimitExceededException, AccountHeldException {
        if (status.equals(String.valueOf(Status.HOLD)))
        {
            throw new AccountHeldException();
        }
        if (accountBalance + overdraftLimit < cmd.getAmount()) {
            throw new OverdraftLimitExceededException();
        }

        accountBalance -= cmd.getAmount();
        apply(new MoneyWithdrawnEvent(id, cmd.getTransactionId(), cmd.getAmount(), accountBalance - cmd.getAmount()));
    }

    @EventSourcingHandler
    protected void on(AccountHeldEvent accountHeldEvent) {
        this.status = String.valueOf(accountHeldEvent.status);
    }
}
