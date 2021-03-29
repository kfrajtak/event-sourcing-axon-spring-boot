package com.progressivecoder.es.eventsourcingaxonspringboot.queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class QueueListener {
    //static final String queueName = "CommandQueue";

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // generic listener
    /*@Bean
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
    }*/
}
