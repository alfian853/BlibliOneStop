package com.gdn.onestop.service;

import com.gdn.onestop.dto.UserGroupDto;
import com.gdn.onestop.model.ChatModel;
import com.gdn.onestop.entity.User;
import com.gdn.onestop.model.GroupModel;
import com.gdn.onestop.model.MeetingModel;
import com.gdn.onestop.request.CreateGroupRequest;
import com.gdn.onestop.request.ChatSendRequest;
import com.gdn.onestop.request.PostNoteRequest;
import com.gdn.onestop.response.MeetingNoteUpdateResponse;

import java.util.Date;
import java.util.List;

public interface GroupService {

    GroupModel createGroup(User user, CreateGroupRequest request);
    UserGroupDto getGroupData(User user);

    ChatModel addChat(User user, String groupId, ChatSendRequest request);

    List<ChatModel> getGroupChatAfterTime(User user, String groupId, Date afterTime, Integer size);

    List<ChatModel> getGroupChatBeforeTime(User user, String groupId, Date beforeTime, Integer size);

    GroupModel joinGroup(User user, String groupCode);

    Date getUserGroupLastUpdate(User user);

    void leaveGroup(User user, String groupId);

    List<String> getMembers(String groupId);

    boolean isValidMember(User user, String groupId);

    List<MeetingModel> getMeetingListData(User user, String groupId);

    MeetingModel getMeetingData(User user, String groupId, Integer meetingNo);

    MeetingNoteUpdateResponse postMeetingNote(User user, String groupId, PostNoteRequest request);

}
