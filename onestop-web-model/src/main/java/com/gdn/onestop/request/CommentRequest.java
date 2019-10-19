package com.gdn.onestop.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CommentRequest {

    @NotEmpty
    String text;
}
