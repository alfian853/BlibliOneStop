package com.gdn.onestop.repository.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public enum IdeaEntitiyField implements MongoEntityField {
    USERNAME("username", true, true),
    CREATED_AT("createdAt", false, true),
    COMMENT("comments",false,false),
    UP_VOTE("upVoteCount",false,true);

    @Getter
    private String field;

    @Getter
    private boolean isSearchable, isSortable;

    private static List<MongoEntityField> ideaMongoFieldList = Arrays.asList(
            USERNAME,CREATED_AT,COMMENT
    );

    IdeaEntitiyField(String field, boolean isSearchable, boolean isSortable) {
        this.field = field;
        this.isSearchable = isSearchable;
        this.isSortable = isSortable;
    }

    @Override
    public String getField() {
        return this.field;
    }

    @Override
    public List<MongoEntityField> getMongoFieldList() {
        return ideaMongoFieldList;
    }


}
