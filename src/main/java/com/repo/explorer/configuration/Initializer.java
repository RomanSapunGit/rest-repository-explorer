package com.repo.explorer.configuration;

import org.springframework.web.reactive.function.client.WebClient;

public interface Initializer {
    WebClient webClient();
}
