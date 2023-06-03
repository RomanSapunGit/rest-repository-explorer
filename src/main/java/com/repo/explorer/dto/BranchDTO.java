package com.repo.explorer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchDTO {
    @JsonProperty
    private String name;
    @JsonProperty
    private CommitDTO commit;
}
