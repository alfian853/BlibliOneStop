package com.gdn.onestop.repository;

import com.gdn.onestop.entity.IdeaComment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IdeaCommentRepository extends MongoRepository<IdeaComment,String>, IdeaCommentExtension{
}
