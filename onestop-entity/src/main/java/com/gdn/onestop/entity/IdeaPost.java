package com.gdn.onestop.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("idea_posts")
public class IdeaPost extends Post {
    Integer voteUp;
    Integer voteDown;
}
