package com.repo.explorer.service.implementations;

import com.repo.explorer.dto.BranchDTO;
import com.repo.explorer.dto.GithubRepositoryDTO;
import com.repo.explorer.service.interfaces.GithubRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;


@Service
public class GithubRepoServiceImpl implements GithubRepoService {
    @Value("${github.api.base-url}")
    private String githubBaseUrl;

    private final RestTemplate restTemplate;

    @Value("${ACCESS_TOKEN}")
    private String accessToken;

    @Autowired
    public GithubRepoServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<GithubRepositoryDTO> getRepositories(String username) {
        URI url = buildUrl("users", username, "repos");
        RequestEntity<Void> request = buildRequest(url);

        ResponseEntity<GithubRepositoryDTO[]> response = restTemplate.exchange(request, GithubRepositoryDTO[].class);

        GithubRepositoryDTO[] repositories = response.getBody();
        List<GithubRepositoryDTO> filteredRepositories = new ArrayList<>();
        if (repositories != null) {
            ExecutorService executorService = Executors.newFixedThreadPool(10);

            List<CompletableFuture<GithubRepositoryDTO>> futures = Arrays.stream(repositories)
                    .filter(repository -> !repository.isFork())
                    .map(repository -> CompletableFuture.supplyAsync(() -> {
                        String repoFullName = repository.getLogin() + "/" + repository.getName();
                        List<BranchDTO> branches = getRepositoryBranches(repoFullName);
                        repository.setBranches(branches);
                        return repository;
                    }, executorService))
                    .collect(Collectors.toList());

            filteredRepositories = futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());
            executorService.shutdown();
        }
        return filteredRepositories;
    }

    private List<BranchDTO> getRepositoryBranches(String repoFullName) {
        URI url = buildUrl("repos", repoFullName, "branches");
        RequestEntity<Void> request = buildRequest(url);

        ResponseEntity<BranchDTO[]> response = restTemplate.exchange(request, BranchDTO[].class);

        BranchDTO[] branches = response.getBody();
        return branches != null ? Arrays.asList(branches) : new ArrayList<>();
    }

    private URI buildUrl(String... pathSegments) {
        return UriComponentsBuilder.fromUriString(githubBaseUrl)
                .pathSegment(pathSegments)
                .build()
                .toUri();
    }

    private RequestEntity<Void> buildRequest(URI url) {
        HttpHeaders headers = setupHeaders();
        return RequestEntity.get(url)
                .headers(headers)
                .build();
    }

    private HttpHeaders setupHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(accessToken);
        return headers;
    }
}
