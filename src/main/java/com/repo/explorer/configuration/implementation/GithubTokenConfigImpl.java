package com.repo.explorer.configuration.implementation;

import com.repo.explorer.configuration.GithubTokenConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GithubTokenConfigImpl implements GithubTokenConfig {
    @Value("${ACCESS_TOKEN}")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }
}
