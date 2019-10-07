package com.gdn.onestop.repository.impl;

import com.gdn.onestop.entity.IdeaComment;
import com.gdn.onestop.repository.IdeaCommentExtension;
import com.gdn.onestop.repository.enums.IdeaEntitiyField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;

import java.util.List;
import java.util.Objects;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

public class IdeaCommentExtensionImpl implements IdeaCommentExtension {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<IdeaComment.CommentUnit> getPaginatedCommentById(String postId, int page, int itemPerPage) {
        TypedAggregation<IdeaComment> agg = newAggregation(IdeaComment.class,
                match(where("_id").is(postId)), project().andExpression(IdeaEntitiyField.COMMENT.getField())
                        .slice(itemPerPage, (page-1)*itemPerPage)
        );

        AggregationResults<IdeaComment> result = mongoTemplate.aggregate(agg, IdeaComment.class);
        return Objects.requireNonNull(result.getUniqueMappedResult()).getComments();
    }
}
