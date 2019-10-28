package com.gdn.onestop.repository;


import com.gdn.onestop.model.ChatModel;
import com.gdn.onestop.entity.GroupChat;

import java.util.Date;
import java.util.List;

public interface GroupRepositoryExtension {
    void save(GroupChat groupChat);
    void addChat(String groupId, ChatModel chat);
    List<ChatModel> getGroupChatAfterTime(String groupId, Date date, Integer size);
    List<ChatModel> getGroupChatBeforeTime(String groupId, Date date, Integer size);
}
