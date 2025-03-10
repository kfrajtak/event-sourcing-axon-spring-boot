package com.progressivecoder.es.eventsourcingaxonspringboot.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// @EnableSwagger
public class SwaggerConfig {

    /*@Bean
    public Docket apiDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.progressivecoder.es"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }*/

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Event Sourcing using Axon and Spring Boot")
                        .contact(
                                new Contact().name("Saurabh Dashora")
                        )
                        .description("App to demonstrate Event Sourcing using Axon and Spring Boot")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("SpringShop Wiki Documentation")
                        .url("https://springshop.wiki.github.org/docs"));
    }
}

