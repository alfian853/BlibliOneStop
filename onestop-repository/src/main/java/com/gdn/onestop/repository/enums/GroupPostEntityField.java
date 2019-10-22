package com.gdn.onestop.repository.enums;

import java.util.Arrays;
import java.util.List;

public enum GroupPostEntityField implements MongoEntityField {

    MEMBERS("members"),
    CHATS("chats"),
    CHAT_CREATED_AT("createdAt");

    private String field;

    GroupPostEntityField(String field){
        this.field = field;
    }

    private static List<MongoEntityField> groupPostMongoFieldList = Arrays.asList(
        MEMBERS,CHATS, CHAT_CREATED_AT
    );

    @Override
    public String getField() {
        return this.field;
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
