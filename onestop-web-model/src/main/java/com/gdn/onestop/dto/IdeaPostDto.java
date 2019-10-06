package com.gdn.onestop.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class IdeaPostDto {
    String id;
    String username;
    String content;
    Integer commentCount;
    Date createdAt;
    Integer upVoteCount;
    Integer downVoteCount;
    Boolean isMeVoteUp;
    Boolean getIsMeVoteDown;
}
