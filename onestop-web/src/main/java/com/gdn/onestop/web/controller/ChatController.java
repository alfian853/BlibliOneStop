package com.gdn.onestop.web.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdn.onestop.dto.UserGroupDto;
import com.gdn.onestop.entity.User;
import com.gdn.onestop.model.ChatModel;
import com.gdn.onestop.model.GroupChatModel;
import com.gdn.onestop.request.ChatSendRequest;
import com.gdn.onestop.request.GroupChatSendRequest;
import com.gdn.onestop.response.Response;
import com.gdn.onestop.response.ResponseHelper;
import com.gdn.onestop.service.GroupService;
import com.gdn.onestop.service.MessagingService;
import com.gdn.onestop.service.UserService;
import com.gdn.onestop.service.exception.InvalidRequestException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

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

    private String usernameToTopic(String username){
        return username.replace(" ","%20");
    }

    @PostMapping("/group/{groupId}")
    Response<GroupChatModel> pushGroupChat(@PathVariable("groupId") String groupId, @RequestBody GroupChatSendRequest request){
        GroupChatModel chatModel = groupService.addChat(userService.getUserBySession(), groupId, request);
        Map<String,String> data = objectMapper.convertValue(chatModel, mapStringStringType);
        data.put("groupId",groupId);

        messagingService.pushMessageToFirebase("/topics/"+groupId, data);

        return ResponseHelper.isOk(chatModel);
    }

    @GetMapping("/group/{groupId}")
    Response<List<GroupChatModel>> getGroupChats(
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

    @PostMapping("/personal/{username}")
    Response<Map<String, String>> pushPersonalChat(@PathVariable("username") String username, @RequestBody ChatSendRequest request){
        Map<String,String> data = objectMapper.convertValue(request, mapStringStringType);
        User user = userService.getUserBySession();
        data.put("id", UUID.randomUUID().toString());
        data.put("_from", user.getUsername());
        data.put("_to",username);
        data.put("createdAt",new Date().getTime()+"");
        messagingService.pushMessageToFirebase("/topics/"+usernameToTopic(username), data);

        return ResponseHelper.isOk(data);
    }

    @PostMapping("/subscribe")
    Response<Boolean> subcribeGroups(@RequestParam("token") String token){

        List<String> tokenList = Collections.singletonList(token);
        User user = userService.getUserBySession();
        UserGroupDto userGroup = groupService.getGroupData(user);

        userGroup.getGuilds().forEach(guild -> {
            FirebaseMessaging.getInstance().subscribeToTopicAsync(
                    tokenList, "/topics/"+guild.getId()
            );

        });

        userGroup.getSquads().forEach(guild -> {
            FirebaseMessaging.getInstance().subscribeToTopicAsync(
                    tokenList, "/topics/"+guild.getId()
            );
        });

        userGroup.getTribes().forEach(guild -> {
            FirebaseMessaging.getInstance().subscribeToTopicAsync(
                    tokenList, "/topics/"+guild.getId()
            );
        });

        String userTopic = usernameToTopic(user.getUsername());
        System.out.println("subscribe to "+userTopic);

        try {
            FirebaseMessaging.getInstance().subscribeToTopic(tokenList, "/topics/"+userTopic);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }

        return ResponseHelper.isOk(true);
    }

}
