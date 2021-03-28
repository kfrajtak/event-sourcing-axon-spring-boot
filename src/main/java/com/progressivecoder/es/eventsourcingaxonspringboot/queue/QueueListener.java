package com.progressivecoder.es.eventsourcingaxonspringboot.queue;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.extensions.amqp.eventhandling.AMQPMessageConverter;
import org.axonframework.extensions.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Configuration
public class QueueListener {
    static final String queueName = "CommandQueue";

    @Bean
    SpringAMQPMessageSource inputMessageSource(final AMQPMessageConverter messageConverter) {
        return new SpringAMQPMessageSource(messageConverter) {
            @RabbitListener(queues = queueName)
            @Override
            public void onMessage(final Message message, final Channel channel) {
                log.info("received external message: {}, channel: {}", message, channel);
                log.info("body: {}", new String(message.getBody()));
                // message.getBody(), message.getMessageProperties().getHeaders()
                super.onMessage(message, channel);
            }
        };
    }
    /*@Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "onMessage");
    }*/
}
