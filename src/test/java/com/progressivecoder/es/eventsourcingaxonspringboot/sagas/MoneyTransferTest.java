package com.progressivecoder.es.eventsourcingaxonspringboot.sagas;

import com.progressivecoder.es.eventsourcingaxonspringboot.aggregates.MoneyTransferAggregate;
import com.progressivecoder.es.eventsourcingaxonspringboot.commands.CompleteMoneyTransferCommand;
import com.progressivecoder.es.eventsourcingaxonspringboot.commands.RequestMoneyTransferCommand;
import com.progressivecoder.es.eventsourcingaxonspringboot.events.MoneyTransferCompletedEvent;
import com.progressivecoder.es.eventsourcingaxonspringboot.events.MoneyTransferRequestedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

// https://docs.axoniq.io/axon-framework-reference/4.10/testing/sagas-1/
public class MoneyTransferTest {
    private FixtureConfiguration<MoneyTransferAggregate> fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new AggregateTestFixture<>(MoneyTransferAggregate.class);
    }

    @Test
    public void createNewTransaction() throws Exception {
        fixture.givenNoPriorActivity()
                .when(new RequestMoneyTransferCommand("abcd", "1234", "2345", 100))
                .expectEvents(new MoneyTransferRequestedEvent("abcd", "1234", "2345", 100));
    }

    @Test
    public void finishTransaction() throws Exception {
        fixture.given(new MoneyTransferRequestedEvent("abcd", "1234", "2345", 100))
                .when(new CompleteMoneyTransferCommand("abcd"))
                .expectEvents(new MoneyTransferCompletedEvent("abcd"));
    }
}
