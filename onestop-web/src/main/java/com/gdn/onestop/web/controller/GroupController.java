package com.gdn.onestop.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdn.onestop.dto.UserGroupDto;
import com.gdn.onestop.entity.User;
import com.gdn.onestop.model.ChatModel;
import com.gdn.onestop.model.GroupModel;
import com.gdn.onestop.request.ChatSendRequest;
import com.gdn.onestop.request.CreateGroupRequest;
import com.gdn.onestop.response.Response;
import com.gdn.onestop.response.ResponseHelper;
import com.gdn.onestop.service.GroupService;
import com.gdn.onestop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController {


    @Autowired
    GroupService groupService;

    @Autowired
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SimpMessagingTemplate template;

    @PostMapping
    Response<GroupModel> createGroup(@RequestBody CreateGroupRequest request){
        return ResponseHelper.isOk(groupService.createGroup(
                userService.getUserBySession(), request)
        );
    }

    @GetMapping("/last_update")
    Response<Date> getUserGroupLastUpdate(){
        return ResponseHelper.isOk(groupService.getUserGroupLastUpdate(userService.getUserBySession()));
    }

    @GetMapping
    Response<UserGroupDto> getGroups(){
        return ResponseHelper.isOk(
                groupService.getGroupData(userService.getUserBySession())
        );
    }

    @PostMapping("/join_group")
    Response<GroupModel> joinGroup(
            @RequestParam("group_code") String groupCode
    ){
        return ResponseHelper.isOk(
                groupService.joinGroup(userService.getUserBySession(), groupCode)
        );
    }

    @PostMapping("/{groupId}/leave")
    Response<Boolean> leaveGroup(@PathVariable("groupId") String groupId){
        groupService.leaveGroup(userService.getUserBySession(), groupId);
        return ResponseHelper.isOk(true);
    }


    @MessageMapping("/chat")
    void addGroupPost(Principal principal, @Payload ChatSendRequest request) throws IOException {
        ChatModel chat = groupService.addChat((User)principal, request.getGroupId(), request);
        template.convertAndSend("/subscribe/chat/"+request.getGroupId(), chat);
    }

    @GetMapping("/{groupId}/chat")
    Response<List<ChatModel>> getGroupChats(
            @PathVariable("groupId") String groupId,
            @RequestParam(value = "after_time", required = false) Long afterTime,
            @RequestParam(value = "before_time", required = false) Long beforeTime,
            @RequestParam(value = "size") Integer size){

        return ResponseHelper.isOk(
                (afterTime != null) ?
                    groupService.getGroupChatAfterTime(userService.getUserBySession(), groupId, new Date(afterTime), size) :
                    groupService.getGroupChatBeforeTime(userService.getUserBySession(), groupId, new Date(beforeTime), size)
        );
    }

}
