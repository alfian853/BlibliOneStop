package com.gdn.onestop.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Data
@Document("idea_posts")
public class IdeaPost extends Post {
    Integer upVoteCount = 0;
    Integer downVoteCount = 0;

    Map<String, Boolean> voter = new HashMap<>();

}
