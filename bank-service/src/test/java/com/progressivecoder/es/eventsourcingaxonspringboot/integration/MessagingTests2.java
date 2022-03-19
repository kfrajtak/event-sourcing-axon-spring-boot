package com.progressivecoder.es.eventsourcingaxonspringboot.integration;

import com.progressivecoder.es.eventsourcingaxonspringboot.dto.commands.AccountCreateDTO;
import com.progressivecoder.es.eventsourcingaxonspringboot.services.commands.AccountCommandService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;
import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = MessagingTests2.Initializer.class)
public class MessagingTests2 {
    // Use @ClassRule to set up something that can be reused by all the test methods, if you can achieve that in a static method.
    @ClassRule
    public static GenericContainer rabbit =
            new GenericContainer("rabbitmq:3.8.14-alpine")
                    .withExposedPorts(5672);

    @ClassRule
    public static GenericContainer axon =
            new GenericContainer("axoniq/axonserver")
                    .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("docker.axonserver")))
                    .withExposedPorts(8024, 8124)
            .waitingFor(Wait.forLogMessage("Started AxonServer", 1));// Wait.forHttp("/actuator/health").forPort(8024));

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @MockBean
    private AccountCommandService accountCommandServiceMock;

    @Test
    public void testQueueListenerVerifyMock() {
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

        await().atMost(5, TimeUnit.SECONDS).until(isMockVerified(), is(true));
    }

    private Callable<Boolean> isMockVerified() {
        ArgumentCaptor<AccountCreateDTO> argument = ArgumentCaptor.forClass(AccountCreateDTO.class);
        return () -> {
            try {
                Mockito.verify(accountCommandServiceMock, Mockito.only())
                        .createAccount(argument.capture());
                assertThat(argument.getValue().getStartingBalance()).isEqualTo(432.1);
                return true;
            } catch (Exception e) {
                return false;
            }
        };
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