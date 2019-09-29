package com.gdn.onestop.repository;

import com.gdn.onestop.entity.IdeaPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface IdeationRepository extends
        MongoRepository<IdeaPost, String>, IdeationRepositoryExtension {
}
