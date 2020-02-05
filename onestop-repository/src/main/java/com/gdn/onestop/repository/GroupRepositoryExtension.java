package com.gdn.onestop.repository;


import com.gdn.onestop.model.GroupChatModel;
import com.gdn.onestop.entity.GroupChat;

import java.util.Date;
import java.util.List;

public interface GroupRepositoryExtension {
    void save(GroupChat groupChat);
    void addChat(String groupId, GroupChatModel chat);
    List<GroupChatModel> getGroupChatAfterTime(String groupId, Date date, Integer size);
    List<GroupChatModel> getGroupChatBeforeTime(String groupId, Date date, Integer size);
}
