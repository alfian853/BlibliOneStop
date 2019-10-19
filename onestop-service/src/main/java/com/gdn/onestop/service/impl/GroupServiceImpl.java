package com.gdn.onestop.service.impl;

import com.gdn.onestop.dto.UserGroupDto;
import com.gdn.onestop.entity.Group;
import com.gdn.onestop.entity.User;
import com.gdn.onestop.entity.UserGroup;
import com.gdn.onestop.model.GroupModel;
import com.gdn.onestop.repository.GroupPostRepository;
import com.gdn.onestop.repository.UserGroupRepository;
import com.gdn.onestop.request.CreateGroupRequest;
import com.gdn.onestop.service.GroupService;
import com.gdn.onestop.service.UserService;
import com.gdn.onestop.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    GroupPostRepository groupRepository;

    @Autowired
    UserGroupRepository userGroupRepository;

    @Autowired
    UserService userService;

    @Override
    public GroupModel createGroup(CreateGroupRequest request) {
        User user = userService.getUserBySession();

        Group group = Group.builder()
                .name(request.getName())
                .type(request.getType())
                .members(Collections.singletonList(user.getId()))
                .build();
        groupRepository.save(group);

        UserGroup userGroup = userGroupRepository.findById(user.getId()).orElseGet(()->{
            UserGroup ug = new UserGroup();
            ug.setId(user.getId());
            ug.setGroups(new LinkedList<>());
            ug.setSquads(new LinkedList<>());
            ug.setTribes(new LinkedList<>());

            return ug;
        });


        GroupModel groupModel = new GroupModel(group.getId(), group.getName());

        switch (request.getType()){
            case GROUP:
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

        return new GroupModel(group.getId(),group.getName());
    }

    @Override
    public UserGroupDto getGroupData() {

        User user = userService.getUserBySession();
        UserGroup userGroup = userGroupRepository.findById(user.getId())
                .orElseThrow(NotFoundException::new);

        return UserGroupDto.builder()
                .username(user.getUsername())
                .groups(userGroup.getGroups())
                .squads(userGroup.getSquads())
                .tribes(userGroup.getTribes())
                .build();
    }
}
