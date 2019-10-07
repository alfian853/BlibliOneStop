package com.gdn.onestop.repository.enums;

import java.util.List;

public interface MongoEntityField {
    String getField();
    boolean isSortable();
    boolean isSearchable();
    List<MongoEntityField> getMongoFieldList();
}
