package com.repo.explorer.controller;

import com.repo.explorer.dto.GithubRepositoryDTO;
import com.repo.explorer.service.implementations.GithubRepoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class GithubRepoController {

    private final GithubRepoServiceImpl githubRepoServiceImpl;

    @Autowired
    public GithubRepoController(GithubRepoServiceImpl githubRepoServiceImpl) {
        this.githubRepoServiceImpl = githubRepoServiceImpl;
    }

    @GetMapping(value = "/repositories/{username}", produces = {MediaType.APPLICATION_JSON_VALUE, "!" + MediaType.APPLICATION_XML_VALUE})
    public List<GithubRepositoryDTO> getRepositories(@PathVariable String username) {
        return githubRepoServiceImpl.getRepositories(username);
    }
}
