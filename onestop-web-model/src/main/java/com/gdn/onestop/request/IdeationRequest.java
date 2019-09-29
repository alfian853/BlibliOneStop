package com.gdn.onestop.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class IdeationRequest {

    @NotBlank
    String content;
}
