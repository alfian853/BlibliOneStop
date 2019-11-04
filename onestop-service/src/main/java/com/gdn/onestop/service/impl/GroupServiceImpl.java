package com.gdn.onestop.service.impl;

import com.gdn.onestop.dto.UserGroupDto;
import com.gdn.onestop.entity.*;
import com.gdn.onestop.model.ChatModel;
import com.gdn.onestop.model.GroupModel;
import com.gdn.onestop.model.GroupType;
import com.gdn.onestop.repository.*;
import com.gdn.onestop.request.CreateGroupRequest;
import com.gdn.onestop.request.ChatSendRequest;
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
            ug.setGuilds(new LinkedList<>());
            ug.setSquads(new LinkedList<>());
            ug.setTribes(new LinkedList<>());

            userGroupRepository.save(ug);
            return ug;
        });
    }

    private GroupModel groupToGroupModel(Group group){
        return new GroupModel(
                group.getId(),
                group.getName(),
                group.getGroupCode(),
                group.getType()
        );
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

        switch (request.getType()){
            case GUILD:
                userGroup.getGuilds().add(group.getId());
                break;
            case SQUAD:
                userGroup.getSquads().add(group.getId());
                break;
            case TRIBE:
                userGroup.getTribes().add(group.getId());
                break;
        }

        userGroupRepository.save(userGroup);


        return groupToGroupModel(group);
    }

    @Override
    public UserGroupDto getGroupData(User user) {

        UserGroup userGroup = getUserGroupByUserId(user.getId());

        UserGroupDto response = new UserGroupDto();

        System.out.println(userGroup.getGuilds().size()+userGroup.getSquads().size()+userGroup.getTribes().size());
        userGroup.getTribes().forEach(
                tribeId -> response.getTribes().add(groupToGroupModel(groupRepository.findGroupById(tribeId)))
        );
        userGroup.getSquads().forEach(
                squadId -> response.getSquads().add(groupToGroupModel(groupRepository.findGroupById(squadId)))
        );
        userGroup.getGuilds().forEach(
                guildId -> response.getGuilds().add(groupToGroupModel(groupRepository.findGroupById(guildId)))
        );

        response.setLastUpdate(userGroup.getUpdatedAt());
        response.setUsername(user.getUsername());

        return response;
    }

    @Override
    public ChatModel addChat(User user, String groupId, ChatSendRequest request) {

        checkMemberValidity(user, groupId);

        ChatModel chat = ChatModel.builder()
                .username(user.getUsername())
                .id(UUID.randomUUID().toString())
                .text(request.getText())
                .createdAt(new Date())
                .isMeeting(request.getIsMeeting())
                .isReply(request.getIsReply())
                .repliedText(request.getRepliedText())
                .repliedId(request.getRepliedId())
                .repliedUsername(request.getRepliedUsername())
                .meetingDate(request.getMeetingDate())
                .build();

        groupRepository.addChat(groupId, chat);

        return chat;
    }

    @Override
    public List<ChatModel> getGroupChatAfterTime(User user, String groupId, Date afterTime, Integer size) {
        checkMemberValidity(user, groupId);
        return groupRepository.getGroupChatAfterTime(groupId, afterTime, size);
    }

    @Override
    public List<ChatModel> getGroupChatBeforeTime(User user, String groupId, Date beforeTime, Integer size) {
        checkMemberValidity(user, groupId);
        return groupRepository.getGroupChatBeforeTime(groupId, beforeTime, size);
    }

    @Override
    public GroupModel joinGroup(User user, String groupCode) {

        Group group = groupRepository.findGroupByGroupCode(groupCode).orElseThrow(NotFoundException::new);

        UserGroup userGroup = getUserGroupByUserId(user.getId());

        boolean alreadyJoin = false;

        if(group.getType().equals(GroupType.TRIBE)){
            alreadyJoin = userGroup.getTribes().stream()
                    .anyMatch(groupId -> groupId.equals(group.getId()));
        }
        else if(group.getType().equals(GroupType.SQUAD)){
            alreadyJoin = userGroup.getSquads().stream()
                    .anyMatch(groupId -> groupId.equals(group.getId()));
        }
        else{
            alreadyJoin = userGroup.getGuilds().stream()
                    .anyMatch(groupId -> groupId.equals(group.getId()));
        }

        if(!alreadyJoin){
            group.getMembers().add(user.getUsername());
            userGroup.getGroupByType(group.getType()).add(group.getId());
            groupRepository.save(group);
            userGroupRepository.save(userGroup);
        }

        return groupToGroupModel(group);
    }

    @Override
    public Date getUserGroupLastUpdate(User user) {
        return getUserGroupByUserId(user.getId()).getUpdatedAt();
    }

    @Override
    public void leaveGroup(User user, String groupId) {
        Group group = groupRepository.findGroupById(groupId);

        group.getMembers().remove(user.getUsername());

        List<String> groups = null;

        UserGroup userGroup = userGroupRepository.findById(user.getId()).get();

        switch (group.getType()){
            case GUILD:
                groups = userGroup.getGuilds();
                break;
            case SQUAD:
                groups = userGroup.getSquads();
                break;
            case TRIBE:
                groups = userGroup.getTribes();
                break;
        }
        groups.remove(groupId);

        userGroupRepository.save(userGroup);
        groupRepository.save(group);
    }


    @Override
    public boolean isValidMember(User user, String groupId) {

        UserGroup userGroup = getUserGroupByUserId(user.getId());

        Group group = groupRepository.findById(groupId).orElseThrow(NotFoundException::new);

        List<String> groupList = null;

        switch (group.getType()){
            case TRIBE:
                groupList = userGroup.getTribes();
                break;
            case SQUAD:
                groupList = userGroup.getSquads();
                break;
            case GUILD:
                groupList = userGroup.getGuilds();
                break;
        }

        return groupList.stream().anyMatch(_groupId -> _groupId.equals(groupId));
    }

    private void checkMemberValidity(User user, String groupId){
        if(!isValidMember(user, groupId))throw new InvalidRequestException("Not a group member");
    }


}
