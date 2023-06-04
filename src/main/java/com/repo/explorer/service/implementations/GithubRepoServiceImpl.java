package com.repo.explorer.service.implementations;

import com.repo.explorer.dto.BranchDTO;
import com.repo.explorer.dto.GithubRepositoryDTO;
import com.repo.explorer.service.interfaces.GithubRepoService;
import com.repo.explorer.util.implementations.URLBuilderImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@Service
public class GithubRepoServiceImpl implements GithubRepoService {
    @Value("${ACCESS_TOKEN}")
    private String accessToken;

    private final WebClient webClient;
    private final URLBuilderImpl urlBuilderImpl;

    public GithubRepoServiceImpl(WebClient.Builder webClientBuilder, URLBuilderImpl urlBuilderImpl) {
        this.webClient = webClientBuilder.build();
        this.urlBuilderImpl = urlBuilderImpl;
    }

    @Override
    public List<GithubRepositoryDTO> getRepositories(String username) {
        var url = urlBuilderImpl.buildUrl("users", username, "repos");
        var repositories = retrieveResponse(url, GithubRepositoryDTO[].class);

        List<GithubRepositoryDTO> filteredRepositories = new ArrayList<>();
        if (repositories != null) {
            var futures = filterRepositories(repositories);

            filteredRepositories = futures.stream()
                    .map(CompletableFuture::join)
                    .toList();
        }
        return filteredRepositories;
    }

    private List<CompletableFuture<GithubRepositoryDTO>> filterRepositories(GithubRepositoryDTO[] repositories) {
        var executorService = Executors.newFixedThreadPool(10);

        return Arrays.stream(repositories)
                .filter(repository -> !repository.fork())
                .map(repository -> CompletableFuture.supplyAsync(() -> {
                    var repoFullName = repository.getLogin() + "/" + repository.name();
                    var branches = getRepositoryBranches(repoFullName);
                    return new GithubRepositoryDTO(repository.name(), repository.owner(), false, branches);
                }, executorService))
                .toList();
    }

    private List<BranchDTO> getRepositoryBranches(String repoFullName) {
        var url = urlBuilderImpl.buildUrl("repos", repoFullName, "branches");
        var branches = retrieveResponse(url, BranchDTO[].class);
        return branches != null ? Arrays.asList(branches) : new ArrayList<>();
    }

    private <T> T retrieveResponse(URI url, Class<T> responseType) {
        return webClient.get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }
}
