package com.gdn.onestop.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class IdeaPost {

    @Id
    String id;

    String username;
    String content;

    Integer totalComment;

    @CreatedDate
    Date createdAt;

    Integer upVoteCount = 0;
    Integer downVoteCount = 0;

    Map<String, Boolean> voter = new HashMap<>();

}
