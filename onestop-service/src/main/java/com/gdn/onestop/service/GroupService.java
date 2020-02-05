package com.gdn.onestop.service;

import com.gdn.onestop.dto.UserGroupDto;
import com.gdn.onestop.model.GroupChatModel;
import com.gdn.onestop.entity.User;
import com.gdn.onestop.model.GroupModel;
import com.gdn.onestop.model.MeetingModel;
import com.gdn.onestop.request.CreateGroupRequest;
import com.gdn.onestop.request.GroupChatSendRequest;
import com.gdn.onestop.request.PostNoteRequest;
import com.gdn.onestop.response.MeetingNoteUpdateResponse;

import java.util.Date;
import java.util.List;

public interface GroupService {

    GroupModel createGroup(User user, CreateGroupRequest request);
    UserGroupDto getGroupData(User user);

    GroupChatModel addChat(User user, String groupId, GroupChatSendRequest request);

    List<GroupChatModel> getGroupChatAfterTime(User user, String groupId, Date afterTime, Integer size);

    List<GroupChatModel> getGroupChatBeforeTime(User user, String groupId, Date beforeTime, Integer size);

    GroupModel joinGroup(User user, String groupCode);

    Date getUserGroupLastUpdate(User user);

    void leaveGroup(User user, String groupId);

    List<String> getMembers(String groupId);

    boolean isValidMember(User user, String groupId);

    List<MeetingModel> getMeetingListData(User user, String groupId);

    MeetingModel getMeetingData(User user, String groupId, Integer meetingNo);

    MeetingNoteUpdateResponse postMeetingNote(User user, String groupId, PostNoteRequest request);

}
