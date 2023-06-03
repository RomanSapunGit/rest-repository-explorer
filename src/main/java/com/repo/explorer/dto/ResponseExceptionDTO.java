package com.repo.explorer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ResponseExceptionDTO {
   @JsonProperty
   private int status;

   @JsonProperty
   private String message;
}
