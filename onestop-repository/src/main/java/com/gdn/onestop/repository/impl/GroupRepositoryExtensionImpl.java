package com.gdn.onestop.repository.impl;

import com.gdn.onestop.entity.GroupPost;
import com.gdn.onestop.repository.GroupRepositoryExtension;
import static com.gdn.onestop.repository.enums.GroupPostEntityField.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;

import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;


public class GroupRepositoryExtensionImpl implements GroupRepositoryExtension {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public GroupPost getPaginatedPost(String groupId, int page, int itemPerPage) {

        TypedAggregation<GroupPost> agg = newAggregation(GroupPost.class,
                match(where("_id").is(groupId)),
                project().andExclude(
                        POSTS.getField(), MEETING_POSTS.getField()
                ).andExpression(POSTS_INFO.getField())
                .slice(itemPerPage, (page-1)*itemPerPage)
        );

        AggregationResults<GroupPost> tmpResult = mongoTemplate.aggregate(agg, GroupPost.class);
        if(tmpResult.getUniqueMappedResult() == null)return null;
        GroupPost tmpGroupPost = tmpResult.getUniqueMappedResult();

        AtomicInteger postCount = new AtomicInteger();
        AtomicInteger meetingCount = new AtomicInteger();

        tmpGroupPost.getPostsInfo().forEach(postInfo -> {
            switch (postInfo.getType()){
                case POST:
                    postCount.getAndIncrement();
                    break;
                case MEETING:
                    meetingCount.getAndIncrement();
            }
        });

        agg = newAggregation(GroupPost.class,
                match(where("_id").is(groupId)),
                project().andExclude(POSTS_INFO.getField()),
                project().andExpression(POSTS.getField()).slice(postCount.get()),
                project().andExpression(MEETING_POSTS.getField()).slice(meetingCount.get())
        );

        AggregationResults<GroupPost> result = mongoTemplate.aggregate(agg, GroupPost.class);
        if(result.getUniqueMappedResult() == null)return null;
        result.getUniqueMappedResult().setPostsInfo(tmpGroupPost.getPostsInfo());
        return result.getUniqueMappedResult();
    }
}
