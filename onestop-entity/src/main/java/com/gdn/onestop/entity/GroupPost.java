package com.gdn.onestop.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;
import java.util.List;

@Data
@Document
public class GroupPost {

    // groupId
    @Id
    String id;

    List<PostInfo> postsInfo;

    Integer postCount;
    Integer meetingPostCount;


    public enum PostType {
        POST, MEETING;
    }

    @Data
    public static class PostInfo {
        Integer index;
        PostType type;
    }

    // use linkedlist for better performance because every new item will be append to front of list
    LinkedList<MeetingPost> meetingPosts;
    LinkedList<Post> posts;
}
