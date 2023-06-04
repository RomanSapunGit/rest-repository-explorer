package com.repo.explorer;

import com.repo.explorer.controller.GithubRepoController;
import com.repo.explorer.dto.GithubRepositoryDTO;
import com.repo.explorer.service.interfaces.GithubRepoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
class RestTaskApplicationTests {
    private static final String GITHUB_USERNAME = "RomanSapunGit";
    private final GithubRepoService githubService;
    private final MockMvc mockMvc;
    WebClient webClient;
    GithubRepoController githubRepoController;

    @Autowired
    public RestTaskApplicationTests(WebClient webClient, GithubRepoService githubService,
                                    GithubRepoController githubRepoController, MockMvc mockMvc) {
        this.githubRepoController = githubRepoController;
        this.webClient = webClient;
        this.githubService = githubService;
        this.mockMvc = mockMvc;
    }

    @Test
    public void testGetRepositories_NotForks() throws ExecutionException, InterruptedException {
        List<GithubRepositoryDTO> repositories = githubService.getRepositories("Spring");

        for (GithubRepositoryDTO repository : repositories) {
            assertFalse(repository.fork());
        }
    }

    @Test
    public void testGetRepositories_UserNotExist() {
        String nonExistingUsername = GITHUB_USERNAME + "12";

        WebClientResponseException.NotFound exception = assertThrows(WebClientResponseException.NotFound.class, () ->
                githubService.getRepositories(nonExistingUsername));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void testGetRepositories_SupportsXMLHeader_false() throws Exception {
        String acceptHeader = "application/xml";

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/repositories/{username}", GITHUB_USERNAME)
                .header("Accept", acceptHeader);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable())
                .andExpect(MockMvcResultMatchers.content().json("{\"status\": 406, \"message\": \"No acceptable representation\"}"))
                .andReturn();
    }
}
