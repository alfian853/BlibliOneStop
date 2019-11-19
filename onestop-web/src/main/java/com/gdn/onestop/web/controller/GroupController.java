package com.gdn.onestop.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdn.onestop.dto.UserGroupDto;
import com.gdn.onestop.entity.User;
import com.gdn.onestop.model.ChatModel;
import com.gdn.onestop.model.GroupModel;
import com.gdn.onestop.model.MeetingModel;
import com.gdn.onestop.request.ChatSendRequest;
import com.gdn.onestop.request.CreateGroupRequest;
import com.gdn.onestop.request.PostNoteRequest;
import com.gdn.onestop.response.MeetingNoteUpdateResponse;
import com.gdn.onestop.response.Response;
import com.gdn.onestop.response.ResponseHelper;
import com.gdn.onestop.service.GroupService;
import com.gdn.onestop.service.MessagingService;
import com.gdn.onestop.service.UserService;
import com.gdn.onestop.service.exception.InvalidRequestException;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
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

    @PostMapping("/{groupId}/chat")
    Response<ChatModel> pushGroupChat(@PathVariable("groupId") String groupId,@RequestBody ChatSendRequest request){
        ChatModel chatModel = groupService.addChat(userService.getUserBySession(), groupId, request);
        Map<String,String> data = objectMapper.convertValue(chatModel, mapStringStringType);
        data.put("groupId",groupId);

        messagingService.pushMessageToFirebase("/topics/"+groupId, data);

        return ResponseHelper.isOk(chatModel);
    }


    /**
     * Unused class/function, for archive purpose
     */
    @MessageMapping("/chat")
    void addGroupPost(Principal principal, @Payload ChatSendRequest request) {
        ChatModel chat = groupService.addChat((User)principal, request.getGroupId(), request);
        template.convertAndSend("/subscribe/chat/"+request.getGroupId(), chat);
    }

    @GetMapping("/{groupId}/chat")
    Response<List<ChatModel>> getGroupChats(
            @PathVariable("groupId") String groupId,
            @RequestParam(value = "after_time", required = false) Long afterTime,
            @RequestParam(value = "before_time", required = false) Long beforeTime,
            @RequestParam(value = "size") Integer size){
        if((afterTime == null) == (beforeTime == null))
            throw new InvalidRequestException("required parameter either after_time or before_time");
        return ResponseHelper.isOk(
                (afterTime != null) ?
                        groupService.getGroupChatAfterTime(userService.getUserBySession(), groupId, new Date(afterTime), size) :
                        groupService.getGroupChatBeforeTime(userService.getUserBySession(), groupId, new Date(beforeTime), size)
        );
    }

    @PostMapping("/subscribe")
    Response<Boolean> subcribeGroups(@RequestParam("token") String token){

        UserGroupDto userGroup = groupService.getGroupData(userService.getUserBySession());
        userGroup.getGuilds().forEach(guild -> {
            FirebaseMessaging.getInstance().subscribeToTopicAsync(
                    Collections.singletonList(token), "/topics/"+guild.getId()
            );
            System.out.println("subscribe to topic "+guild.getId());

        });
        userGroup.getSquads().forEach(guild -> {
            FirebaseMessaging.getInstance().subscribeToTopicAsync(
                    Collections.singletonList(token), "/topics/"+guild.getId()
            );
            System.out.println("subscribe to topic "+guild.getId());
        });

        userGroup.getTribes().forEach(guild -> {
            FirebaseMessaging.getInstance().subscribeToTopicAsync(
                    Collections.singletonList(token), "/topics/"+guild.getId()
            );
        });

        return ResponseHelper.isOk(true);
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

}
