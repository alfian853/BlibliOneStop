package com.gdn.onestop.repository;


import com.gdn.onestop.entity.Chat;
import com.gdn.onestop.entity.GroupChat;

import java.util.Date;
import java.util.List;

public interface GroupRepositoryExtension {
    void save(GroupChat groupChat);
    void addChat(String groupId, Chat chat);
    List<Chat> getGroupChatAfterTime(String groupId, Date date);
}
