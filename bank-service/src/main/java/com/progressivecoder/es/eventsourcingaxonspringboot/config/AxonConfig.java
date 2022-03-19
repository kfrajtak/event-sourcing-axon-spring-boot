package com.progressivecoder.es.eventsourcingaxonspringboot.config;

//import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class AxonConfig {
/*
    @Value("${axon.amqp.exchange}")
    private String exchange;

    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.fanoutExchange(exchange).build();
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(exchange).build();
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with("*").noargs();
    }

    @Autowired
    public void configure(AmqpAdmin amqpAdmin) {
        amqpAdmin.declareExchange(exchange());

        amqpAdmin.declareQueue(queue());
        amqpAdmin.declareBinding(binding());
    }*/
}
