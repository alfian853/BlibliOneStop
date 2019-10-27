package com.gdn.onestop.repository.impl;

import com.gdn.onestop.model.ChatModel;
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
    public void addChat(String groupId, ChatModel chat) {

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
    public List<ChatModel> getGroupChatAfterTime(String groupId, Date date, Integer size) {
        TypedAggregation<GroupChat> agg = newAggregation(GroupChat.class,
                match(where("_id").is(groupId)),
                unwind("$"+CHATS.getField()),
                match(Criteria.where(CHATS.getField()+"."+CHAT_CREATED_AT.getField()).gt(date)),
                group("id").push("$"+CHATS.getField()).as(CHATS.getField()),
                project().andExpression(CHATS.getField()).slice(size,-size)
        );

        AggregationResults<GroupChat> result = mongoTemplate.aggregate(agg, GroupChat.class);

        GroupChat groupChat = result.getUniqueMappedResult();

        if(groupChat == null){
            return new LinkedList<>();
        }

        return groupChat.getChats();
    }

    @Override
    public List<ChatModel> getGroupChatBeforeTime(String groupId, Date date, Integer size) {
        TypedAggregation<GroupChat> agg = newAggregation(GroupChat.class,
                match(where("_id").is(groupId)),
                unwind("$"+CHATS.getField()),
                match(Criteria.where(CHATS.getField()+"."+CHAT_CREATED_AT.getField()).lt(date)),
                group("id").push("$"+CHATS.getField()).as(CHATS.getField()),
                project().andExpression(CHATS.getField()).slice(size)
        );

        AggregationResults<GroupChat> result = mongoTemplate.aggregate(agg, GroupChat.class);

        GroupChat groupChat = result.getUniqueMappedResult();

        if(groupChat == null){
            return new LinkedList<>();
        }

        return groupChat.getChats();
    }

}
