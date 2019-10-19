package com.gdn.onestop.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.LinkedList;

@Data
@Builder
@Document
public class IdeaComment {

    @Id
    String id;

    // use linkedlist for better performance because every new item will be append to front of list
    LinkedList<CommentUnit> comments;

    @Data
    @Builder
    public static class CommentUnit {
        String username;
        String text;
        Date date;
    }
}
