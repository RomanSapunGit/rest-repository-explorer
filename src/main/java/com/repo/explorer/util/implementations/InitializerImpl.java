package com.repo.explorer.util.implementations;

import com.repo.explorer.util.interfaces.Initializer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class InitializerImpl implements Initializer {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
