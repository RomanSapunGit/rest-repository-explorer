package com.repo.explorer.util.implementations;

import com.repo.explorer.util.interfaces.URLBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class URLBuilderImpl implements URLBuilder {
    @Value("${github.api.base-url}")
    private String githubBaseUrl;

    public URI buildUrl(String... pathSegments) {
        return UriComponentsBuilder.fromUriString(githubBaseUrl)
                .pathSegment(pathSegments)
                .build()
                .toUri();
    }
}
