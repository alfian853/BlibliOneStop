package com.gdn.onestop.service;

import com.gdn.onestop.dto.UserGroupDto;
import com.gdn.onestop.entity.Chat;
import com.gdn.onestop.entity.User;
import com.gdn.onestop.model.GroupModel;
import com.gdn.onestop.request.CreateGroupRequest;
import com.gdn.onestop.request.ChatRequest;

import java.util.Date;
import java.util.List;

public interface GroupService {

    GroupModel createGroup(User user, CreateGroupRequest request);
    UserGroupDto getGroupData(User user);

    Chat addChat(User user, String groupId, ChatRequest request);

    List<Chat> getGroupChat(User user, String groupId, Date fromTime);

    GroupModel joinGroup(User user, String groupCode);

    Date getUserGroupLastUpdate(User user);

    void leaveGroup(User user, String groupId);
}
