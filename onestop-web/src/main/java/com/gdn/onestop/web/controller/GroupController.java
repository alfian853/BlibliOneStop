package com.gdn.onestop.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdn.onestop.dto.UserGroupDto;
import com.gdn.onestop.entity.User;
import com.gdn.onestop.model.GroupChatModel;
import com.gdn.onestop.model.GroupModel;
import com.gdn.onestop.model.MeetingModel;
import com.gdn.onestop.request.GroupChatSendRequest;
import com.gdn.onestop.request.CreateGroupRequest;
import com.gdn.onestop.request.PostNoteRequest;
import com.gdn.onestop.response.MeetingNoteUpdateResponse;
import com.gdn.onestop.response.Response;
import com.gdn.onestop.response.ResponseHelper;
import com.gdn.onestop.service.GroupService;
import com.gdn.onestop.service.MessagingService;
import com.gdn.onestop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/group")
public class GroupController {


    @Autowired
    GroupService groupService;

    @Autowired
    UserService userService;

    @Qualifier("objectMapperIgnoreNull")
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SimpMessagingTemplate template;

    @Autowired
    MessagingService messagingService;

    private TypeReference<Map<String,String>> mapStringStringType = new TypeReference<Map<String,String>>(){};

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

    /**
     * Unused class/function, for archive purpose
     */
    @MessageMapping("/chat")
    void addGroupPost(Principal principal, @Payload GroupChatSendRequest request) {
        GroupChatModel chat = groupService.addChat((User)principal, request.getGroupId(), request);
        template.convertAndSend("/subscribe/chat/"+request.getGroupId(), chat);
    }

    @GetMapping("/{groupId}/meeting")
    Response<List<MeetingModel>> getMeetingListData(@PathVariable("groupId") String groupId){
        return ResponseHelper.isOk(
                groupService.getMeetingListData(userService.getUserBySession(), groupId)
        );
    }

    @PostMapping("/{groupId}/meeting/note")
    Response<MeetingNoteUpdateResponse> updateMeetingNote(
            @PathVariable("groupId") String groupId,
            @RequestBody PostNoteRequest request){
        return ResponseHelper.isOk(
            groupService.postMeetingNote(userService.getUserBySession(), groupId, request)
        );
    }

    @GetMapping("/{groupId}/meeting/{meetingNo}")
    Response<MeetingModel> getMeetingData(
            @PathVariable("groupId") String groupId,
            @PathVariable("meetingNo") Integer meetingNo){

        return ResponseHelper.isOk(
            groupService.getMeetingData(userService.getUserBySession(), groupId, meetingNo)
        );
    }

    @GetMapping("/{groupId}/members")
    Response<List<String>> getMembers(@PathVariable("groupId") String groupId){
        return ResponseHelper.isOk(groupService.getMembers(groupId));
    }

}
