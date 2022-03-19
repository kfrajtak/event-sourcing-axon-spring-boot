package com.progressivecoder.es.eventsourcingaxonspringboot.config;
/*
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.extensions.amqp.eventhandling.AMQPMessageConverter;
import org.axonframework.extensions.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@ProcessingGroup("amqpEvents")
@Component("myQueueMessageSource")
public class RabbitMQSpringAMQPMessageSource extends SpringAMQPMessageSource {

    @Autowired
    public RabbitMQSpringAMQPMessageSource(final AMQPMessageConverter messageConverter) {
        super(messageConverter);
    }

    @RabbitListener(queues = "${axon.amqp.queue}")
    @Override
    public void onMessage(final Message message, final Channel channel) {
        log.info("received message: {}, {}, channel={}", new String(message.getBody()), message, channel);
        super.onMessage(message, channel);
    }
}
*/