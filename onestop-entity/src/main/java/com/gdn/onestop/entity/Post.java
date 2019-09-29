package com.gdn.onestop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    String id;

    String username;
    String content;

    Integer totalComment;
    List<Comment> comments;

    @CreatedDate
    Date createdAt;
}

@Data
class Comment {
    String username;
    String text;
    Integer likes;
}

