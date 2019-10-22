package com.gdn.onestop.repository;

import com.gdn.onestop.entity.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<Group, String>, GroupRepositoryExtension {

}
