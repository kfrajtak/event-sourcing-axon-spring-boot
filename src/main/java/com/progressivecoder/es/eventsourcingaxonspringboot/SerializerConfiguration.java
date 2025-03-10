package com.progressivecoder.es.eventsourcingaxonspringboot;

import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SerializerConfiguration {
    @Bean
    @Primary
    public Serializer defaultSerializer() {
        return JacksonSerializer.builder().build();
    }

    @Bean
    @Qualifier("eventSerializer")
    public Serializer eventSerializer() {
        return JacksonSerializer.builder().build();
    }

    @Bean
    @Qualifier("messageSerializer")
    public Serializer messageSerializer() {
        return JacksonSerializer.builder().build();
    }
}
