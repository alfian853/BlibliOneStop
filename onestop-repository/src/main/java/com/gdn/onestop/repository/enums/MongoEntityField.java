package com.gdn.onestop.repository.enums;

import java.util.List;

public interface MongoEntityField {
    String getMongoFieldValue();
    List<MongoEntityField> getMongoFieldList();
}
