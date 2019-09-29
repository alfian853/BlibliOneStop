package com.gdn.onestop.repository.impl;

import com.gdn.onestop.entity.IdeaPost;
import com.gdn.onestop.repository.IdeationRepositoryExtension;
import com.gdn.onestop.repository.enums.IdeaEntitiyField;
import com.gdn.onestop.repository.enums.MongoEntityField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

public class IdeationRepositoryExtensionImpl implements IdeationRepositoryExtension {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    @Override
    public Class<IdeaPost> getEntityClass() {
        return IdeaPost.class;
    }

    @Override
    public List<? extends MongoEntityField> getFieldList() {
        return IdeaEntitiyField.USERNAME.getMongoFieldList();
    }
}
