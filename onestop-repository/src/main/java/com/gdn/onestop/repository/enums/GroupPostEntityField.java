package com.gdn.onestop.repository.enums;

import java.util.Arrays;
import java.util.List;

public enum GroupPostEntityField implements MongoEntityField {
    POST_COUNT("postCount"),
    MEETING_POST_COUNT("meetingPostCount"),
    POSTS_INFO("postsInfo"),
    MEETING_POSTS("meetingPosts"),
    POSTS("posts");

    private String field;

    GroupPostEntityField(String field){
        field = field;
    }

    private static List<MongoEntityField> groupPostMongoFieldList = Arrays.asList(
        POST_COUNT,MEETING_POST_COUNT,POSTS_INFO,MEETING_POSTS,POSTS
    );

    @Override
    public String getField() {
        return field;
    }

    @Override
    public boolean isSortable() {
        return false;
    }

    @Override
    public boolean isSearchable() {
        return false;
    }

    @Override
    public List<MongoEntityField> getMongoFieldList() {
        return groupPostMongoFieldList;
    }
}
