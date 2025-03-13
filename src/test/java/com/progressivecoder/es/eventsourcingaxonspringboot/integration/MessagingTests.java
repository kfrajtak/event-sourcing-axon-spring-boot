package com.progressivecoder.es.eventsourcingaxonspringboot.integration;

import com.progressivecoder.es.eventsourcingaxonspringboot.dto.commands.AccountCreateDTO;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureRule;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = MessagingTests.Initializer.class)
public class MessagingTests {
    @ClassRule
    public static RabbitMQContainer rabbit =
            new RabbitMQContainer("rabbitmq:3.8.14-alpine")
                    .withExposedPorts(5672);

    @ClassRule
    public static GenericContainer axon =
            new GenericContainer("axoniq/axonserver")
                    .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("docker.axonserver")))
                    .withExposedPorts(8024, 8124)
                    .waitingFor(Wait.forHttp("/actuator/health").forPort(8024));
    @Rule
    OutputCaptureRule outputCaptureRule = new OutputCaptureRule();

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testQueueListenerWaitForConsoleOutput() {
        /*channel.basic_publish(exchange='spring-boot-exchange',
          routing_key='Command',
          properties=pika.BasicProperties(
              content_type="application/json",
              content_encoding='UTF-8',
              headers={
                  '__TypeId__': 'com.progressivecoder.es.eventsourcingaxonspringboot.dto.commands.AccountCreateDTO'}
          ),
          body=json)
        */
        AccountCreateDTO accountCreateDTO = new AccountCreateDTO(432.1, "USD", 0);

        rabbitTemplate.convertAndSend(
                "spring-boot-exchange",
                "Command",
                accountCreateDTO, // will be serialized to JSON (content-type is application/json)
                m -> {
                    m.getMessageProperties().getHeaders().put("__TypeId__", accountCreateDTO.getClass().getName());
                    m.getMessageProperties().setContentType("application/json");
                    m.getMessageProperties().setContentEncoding("UTF-8");
                    return m;
                });

        await().atMost(5, TimeUnit.SECONDS).until(isMessageConsumed(), is(true));
    }

    @Test
    public void testQueueListenerWaitForAccountActivatedEvent() {
        AccountCreateDTO accountCreateDTO = new AccountCreateDTO(432.1, "USD", 0);

        rabbitTemplate.convertAndSend(
                "spring-boot-exchange",
                "Command",
                accountCreateDTO, // will be serialized to JSON (content-type is application/json)
                m -> {
                    m.getMessageProperties().getHeaders().put("__TypeId__", accountCreateDTO.getClass().getName());
                    m.getMessageProperties().setContentType("application/json");
                    m.getMessageProperties().setContentEncoding("UTF-8");
                    return m;
                });

        await().atMost(5, TimeUnit.SECONDS).until(isMessageConsumed(), is(true));
    }

    private Callable<Boolean> isMessageConsumed() {
        return () -> outputCaptureRule.toString().contains("AccountCreateDTO(startingBalance=432.1, currency=USD, overdraftLimit=0.0)");
    }

    @Slf4j
    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            val values = TestPropertyValues.of(
                    "axon.axonserver.servers=" + axon.getContainerIpAddress() + ":" + axon.getMappedPort(8124),
                    "spring.rabbitmq.host=" + rabbit.getContainerIpAddress(),
                    "spring.rabbitmq.port=" + rabbit.getMappedPort(5672)
            );
            log.info(values.toString());
            values.applyTo(configurableApplicationContext);
        }
    }
}