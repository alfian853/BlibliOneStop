package com.gdn.onestop.repository.impl;

import com.gdn.onestop.entity.Chat;
import com.gdn.onestop.entity.GroupChat;
import com.gdn.onestop.repository.GroupRepositoryExtension;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.*;

import static com.gdn.onestop.repository.enums.GroupPostEntityField.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;


public class GroupRepositoryExtensionImpl implements GroupRepositoryExtension {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void save(GroupChat groupChat) {
        mongoTemplate.save(groupChat);
    }

    @Override
    public void addChat(String groupId, Chat chat) {

        Update update = new Update();

        update.push(CHATS.getField())
                .atPosition(0)
                .value(chat);

        Query query = new Query();
        query.addCriteria(where("_id").is(groupId));

        UpdateResult result = mongoTemplate.updateFirst(query, update, GroupChat.class);

        if(result.getMatchedCount() == 0){
            GroupChat groupChat = new GroupChat();
            groupChat.setId(groupId);
            groupChat.setChats(Collections.emptyList());
            mongoTemplate.save(groupChat);

            mongoTemplate.updateFirst(query, update, GroupChat.class);
        }

    }

    @Override
    public List<Chat> getGroupChatAfterTime(String groupId, Date date) {
        TypedAggregation<GroupChat> agg = newAggregation(GroupChat.class,
                match(where("_id").is(groupId)),
                unwind("$"+CHATS.getField()),
                match(Criteria.where(CHATS.getField()+"."+CHAT_CREATED_AT.getField()).gte(date)),
                group("id").push("$"+CHATS.getField()).as(CHATS.getField())
        );

        AggregationResults<GroupChat> result = mongoTemplate.aggregate(agg, GroupChat.class);

        GroupChat groupChat = result.getUniqueMappedResult();

        if(groupChat == null){
            return new LinkedList<>();
        }

        return groupChat.getChats();
    }




//    @Override
//    public GroupPostInfo getPaginatedPost(String groupId, int page, int itemPerPage) {
//
//        TypedAggregation<GroupPostInfo> agg = newAggregation(GroupPostInfo.class,
//                match(where("_id").is(groupId)),
//                project().andExclude(
//                        POSTS.getField(), MEETING_POSTS.getField()
//                ).andExpression(POSTS_INFO.getField())
//                .slice(itemPerPage, (page-1)*itemPerPage)
//        );
//
//        AggregationResults<GroupPostInfo> tmpResult = mongoTemplate.aggregate(agg, GroupPostInfo.class);
//        if(tmpResult.getUniqueMappedResult() == null)return null;
//        GroupPostInfo tmpGroupPostInfo = tmpResult.getUniqueMappedResult();
//
//        AtomicInteger postCount = new AtomicInteger();
//        AtomicInteger meetingCount = new AtomicInteger();
//
//        tmpGroupPostInfo.getPostsInfo().forEach(postInfo -> {
//            switch (postInfo.getType()){
//                case POST:
//                    postCount.getAndIncrement();
//                    break;
//                case MEETING:
//                    meetingCount.getAndIncrement();
//            }
//        });
//
//        agg = newAggregation(GroupPostInfo.class,
//                match(where("_id").is(groupId)),
//                project().andExclude(POSTS_INFO.getField()),
//                project().andExpression(POSTS.getField()).slice(postCount.get()),
//                project().andExpression(MEETING_POSTS.getField()).slice(meetingCount.get())
//        );
//
//        AggregationResults<GroupPostInfo> result = mongoTemplate.aggregate(agg, GroupPostInfo.class);
//        if(result.getUniqueMappedResult() == null)return null;
//        result.getUniqueMappedResult().setPostsInfo(tmpGroupPostInfo.getPostsInfo());
//        return result.getUniqueMappedResult();
//    }
//
//    @Override
//    public void addPost(String groupId, Post post) {
//
//        TypedAggregation<GroupPostInfo> agg = newAggregation(GroupPostInfo.class,
//                match(where("_id").is(groupId)),
//                project().andInclude(POST_COUNT.getField())
//        );
//
//        AggregationResults<GroupPostInfo> result = mongoTemplate.aggregate(agg, GroupPostInfo.class);
//
//        Query query = new Query();
//        query.addCriteria(Criteria.where("_id").is(groupId));
//
//        Update update = new Update();
//        update.inc(POST_COUNT.getField());
//
//
//        update.push(POSTS_INFO.getField())
//                .atPosition(0)
//                .value(new GroupPostInfo.PostInfo(
//                result.getUniqueMappedResult().getPostCount()+1, GroupPostInfo.PostType.POST));
//
//        update.push(POSTS.getField())
//                .atPosition(0)
//                .value(post);
//
//        mongoTemplate.updateFirst(query, update, GroupPostInfo.class);
//
//    }
//
//    @Override
//    public void addMeetingPost(String groupId, MeetingPost meetingPost) {
//        TypedAggregation<GroupPostInfo> agg = newAggregation(GroupPostInfo.class,
//                match(where("_id").is(groupId)),
//                project().andInclude(MEETING_POST_COUNT.getField())
//        );
//
//        AggregationResults<GroupPostInfo> result = mongoTemplate.aggregate(agg, GroupPostInfo.class);
//
//        Query query = new Query();
//        query.addCriteria(Criteria.where("_id").is(groupId));
//
//        Update update = new Update();
//        update.inc(MEETING_POST_COUNT.getField());
//
//
//        update.push(POSTS_INFO.getField())
//                .atPosition(0)
//                .value(new GroupPostInfo.PostInfo(
//                        result.getUniqueMappedResult().getMeetingPostCount()+1, GroupPostInfo.PostType.MEETING));
//
//        update.push(MEETING_POSTS.getField())
//                .atPosition(0)
//                .value(meetingPost);
//
//        mongoTemplate.updateFirst(query, update, GroupPostInfo.class);
//    }
}
