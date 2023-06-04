package com.repo.explorer.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public record GithubRepositoryDTO(String name, OwnerDTO owner, @JsonIgnore boolean fork, List<BranchDTO> branches) {
    @JsonIgnore
    public String getLogin() {
        return owner != null ? owner.login() : null;
    }
}
