package com.repo.explorer.service.implementation;

import com.repo.explorer.configuration.implementation.GithubTokenConfigImpl;
import com.repo.explorer.dto.BranchDTO;
import com.repo.explorer.dto.GithubRepositoryDTO;
import com.repo.explorer.service.GithubRepoService;
import com.repo.explorer.util.implementations.URLBuilderImpl;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class GithubRepoServiceImpl implements GithubRepoService {
    private final WebClient webClient;
    private final URLBuilderImpl urlBuilderImpl;
    private final GithubTokenConfigImpl githubApiConfig;

    public GithubRepoServiceImpl(WebClient webClientBuilder, URLBuilderImpl urlBuilderImpl, GithubTokenConfigImpl githubApiConfig) {
        this.webClient = webClientBuilder;
        this.urlBuilderImpl = urlBuilderImpl;
        this.githubApiConfig = githubApiConfig;
    }

    @Override
    public List<GithubRepositoryDTO> getRepositories(String username) {
        var repoUrl = urlBuilderImpl.buildUrl("users", username, "repos");
        var userRepositories = retrieveResponse(repoUrl, new ParameterizedTypeReference<List<GithubRepositoryDTO>>() {});

        List<GithubRepositoryDTO> filteredRepositories = new ArrayList<>();
        if (userRepositories != null) {
            var futures = filterRepositories(userRepositories);

            filteredRepositories = futures.stream()
                    .map(CompletableFuture::join)
                    .toList();
        }
        return filteredRepositories;
    }

    private List<CompletableFuture<GithubRepositoryDTO>> filterRepositories(List<GithubRepositoryDTO> repositories) {
        var executorService = Executors.newFixedThreadPool(10);

        return repositories.stream()
                .filter(repository -> !repository.fork())
                .map(userRepository -> CompletableFuture.supplyAsync(() -> {
                    var repoFullName = userRepository.getLogin() + "/" + userRepository.name();
                    var userBranches = getRepositoryBranches(repoFullName);
                    return new GithubRepositoryDTO(userRepository.name(), userRepository.owner(), false, userBranches);
                }, executorService))
                .toList();
    }

    private List<BranchDTO> getRepositoryBranches(String repoFullName) {
        var branchUrl = urlBuilderImpl.buildUrl("repos", repoFullName, "branches");
        var userBranches = retrieveResponse(branchUrl, new ParameterizedTypeReference<List<BranchDTO>>() {});
        return userBranches != null ? userBranches : new ArrayList<>();
    }

    private <T> T retrieveResponse(URI url, ParameterizedTypeReference<T> responseType) {
        return webClient.get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(githubApiConfig.getAccessToken()))
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }
}
