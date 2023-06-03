package com.repo.explorer.service.interfaces;

import com.repo.explorer.dto.GithubRepositoryDTO;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface GithubRepoService {
     List<GithubRepositoryDTO> getRepositories(String username) throws ExecutionException, InterruptedException;
}
