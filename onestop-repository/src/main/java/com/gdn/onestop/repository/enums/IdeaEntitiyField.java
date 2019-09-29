package com.gdn.onestop.repository.enums;

import java.util.Arrays;
import java.util.List;

public enum IdeaEntitiyField implements MongoEntityField {
    USERNAME("username"),
    CREATED_AT("createdAt");

    private String field;
    private static List<MongoEntityField> ideaMongoFieldList = Arrays.asList(
            USERNAME,CREATED_AT
    );

    IdeaEntitiyField(String field) {
        this.field = field;
    }

    @Override
    public String getMongoFieldValue() {
        return this.field;
    }

    @Override
    public List<MongoEntityField> getMongoFieldList() {
        return ideaMongoFieldList;
    }
}
