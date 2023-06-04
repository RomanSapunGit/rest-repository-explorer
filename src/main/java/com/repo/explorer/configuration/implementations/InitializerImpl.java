package com.repo.explorer.configuration.implementations;

import com.repo.explorer.configuration.interfaces.Initializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class InitializerImpl implements Initializer {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
