package com.progressivecoder.es.eventsourcingaxonspringboot.queue;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
    static final String topicExchangeName = "spring-boot-exchange";
    static final String queueName = "AccountActivated";

    @Bean
    Queue accountActivatedQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    Queue commandQueue() {
        return new Queue("CommandQueue", true);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding1(Queue accountActivatedQueue, TopicExchange exchange) {
        return BindingBuilder.bind(accountActivatedQueue).to(exchange).with("AccountActivatedEvent");
    }

    @Bean
    Binding binding2(Queue commandQueue, TopicExchange exchange) {
        return BindingBuilder.bind(commandQueue).to(exchange).with("Command");
    }
}
