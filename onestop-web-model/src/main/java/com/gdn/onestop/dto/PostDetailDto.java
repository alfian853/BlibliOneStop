package com.gdn.onestop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostDetailDto {

    String content;
    Date createdAt;

    List<CommentDto> comments;

    @JsonProperty("comment_page")
    Integer commentPage;

    @JsonProperty("total_comment_page")
    private Integer totalCommentPage;

    @JsonProperty("comment_per_page")
    Integer itemPerPage;

    @JsonProperty("total_comment")
    private Integer totalItem;

}
