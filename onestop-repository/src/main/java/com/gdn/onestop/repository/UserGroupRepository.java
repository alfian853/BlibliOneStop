package com.gdn.onestop.repository;

import com.gdn.onestop.entity.UserGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserGroupRepository extends MongoRepository<UserGroup, String> {

}
