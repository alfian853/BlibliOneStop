package com.gdn.onestop.repository;

import com.gdn.onestop.entity.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface GroupRepository extends MongoRepository<Group, String>, GroupRepositoryExtension {

    Group findGroupById(String groupId);
    Optional<Group> findGroupByGroupCode(String groupCode);
}
