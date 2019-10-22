package com.gdn.onestop.service.impl;

import com.gdn.onestop.dto.UserGroupDto;
import com.gdn.onestop.entity.*;
import com.gdn.onestop.model.GroupModel;
import com.gdn.onestop.repository.*;
import com.gdn.onestop.request.CreateGroupRequest;
import com.gdn.onestop.request.ChatRequest;
import com.gdn.onestop.service.GroupService;
import com.gdn.onestop.service.exception.InvalidRequestException;
import com.gdn.onestop.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;


    private UserGroup getUserGroupByUserId(String userId){
        return userGroupRepository.findById(userId).orElseGet(()->{
            UserGroup ug = new UserGroup();
            ug.setId(userId);
            ug.setGroups(new LinkedList<>());
            ug.setSquads(new LinkedList<>());
            ug.setTribes(new LinkedList<>());

            userGroupRepository.save(ug);
            return ug;
        });
    }

    @Override
    public GroupModel createGroup(User user, CreateGroupRequest request) {

        // create group
        Group group = Group.builder()
                .name(request.getName())
                .type(request.getType())
                .groupCode(UUID.randomUUID().toString().substring(0,7))
                .members(Collections.singletonList(user.getId()))
                .build();

        groupRepository.save(group);

        // create groupChat
        GroupChat groupChat = new GroupChat();
        groupChat.setChats(Collections.emptyList());
        groupRepository.save(groupChat);

        UserGroup userGroup = getUserGroupByUserId(user.getId());

        GroupModel groupModel = new GroupModel(group.getId(), group.getName());

        switch (request.getType()){
            case GUILD:
                userGroup.getGroups().add(groupModel);
                break;
            case SQUAD:
                userGroup.getSquads().add(groupModel);
                break;
            case TRIBE:
                userGroup.getTribes().add(groupModel);
                break;
        }

        userGroupRepository.save(userGroup);



        return new GroupModel(group.getId(),group.getName(), group.getType());
    }

    @Override
    public UserGroupDto getGroupData(User user) {

        UserGroup userGroup = getUserGroupByUserId(user.getId());

        return UserGroupDto.builder()
                .username(user.getUsername())
                .groups(userGroup.getGroups())
                .squads(userGroup.getSquads())
                .tribes(userGroup.getTribes())
                .build();
    }

    @Override
    public Chat addChat(User user, String groupId, ChatRequest request) {

        checkMemberValidity(user, groupId);

        Chat chat = Chat.builder()
                .username(user.getUsername())
                .id(UUID.randomUUID().toString())
                .text(request.getText())
                .createdAt(new Date())
                .isMeeting(request.getIsMeeting())
                .isReply(request.getIsReply())
                .repliedText(request.getRepliedText())
                .repliedId(request.getRepliedId())
                .meetingDate(request.getMeetingDate())
                .build();

        groupRepository.addChat(groupId, chat);

        return chat;
    }

    @Override
    public List<Chat> getGroupChat(User user, String groupId, Date fromTime) {

        checkMemberValidity(user, groupId);
        return groupRepository.getGroupChatAfterTime(groupId, fromTime);
    }

    private void checkMemberValidity(User user, String groupId){

        UserGroup userGroup = getUserGroupByUserId(user.getId());

        Group group = groupRepository.findById(groupId).orElseThrow(NotFoundException::new);

        List<GroupModel> groupList = null;

        switch (group.getType()){
            case TRIBE:
                groupList = userGroup.getTribes();
                break;
            case SQUAD:
                groupList = userGroup.getSquads();
                break;
            case GUILD:
                groupList = userGroup.getGroups();
                break;
        }

        boolean isJoined = groupList.stream().anyMatch(groupModel -> groupModel.getId().equals(groupId));

        if(!isJoined)throw new InvalidRequestException("Not a group member");
    }


}
