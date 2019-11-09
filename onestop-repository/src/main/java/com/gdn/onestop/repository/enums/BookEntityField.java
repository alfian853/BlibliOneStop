package com.gdn.onestop.repository.enums;

import java.util.Collections;
import java.util.List;

public enum BookEntityField implements MongoEntityField {
    TITLE("title"),CREATED_AT("createdAt");

    String field;

    BookEntityField(String field) {
        this.field = field;
    }

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
        return Collections.singletonList(TITLE);
    }
}
