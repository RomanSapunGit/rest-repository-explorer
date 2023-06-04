package com.repo.explorer.configuration.interfaces;

import org.springframework.web.reactive.function.client.WebClient;

public interface Initializer {
    WebClient webClient();
}
