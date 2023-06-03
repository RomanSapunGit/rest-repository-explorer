package com.repo.explorer.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GithubRepositoryDTO {
    private String name;
    private OwnerDTO owner;
    @JsonIgnore
    private boolean fork;
    private List<BranchDTO> branches;

    @JsonIgnore
    public String getLogin() {
        return owner != null ? owner.getLogin() : null;
    }
}
