package com.gdn.onestop.web.controller;

import com.gdn.onestop.dto.UserGroupDto;
import com.gdn.onestop.entity.Chat;
import com.gdn.onestop.model.GroupModel;
import com.gdn.onestop.request.CreateGroupRequest;
import com.gdn.onestop.request.ChatRequest;
import com.gdn.onestop.response.Response;
import com.gdn.onestop.response.ResponseHelper;
import com.gdn.onestop.service.GroupService;
import com.gdn.onestop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController {


    @Autowired
    GroupService groupService;

    @Autowired
    UserService userService;

    @PostMapping
    Response<GroupModel> createGroup(@RequestBody CreateGroupRequest request){
        return ResponseHelper.isOk(groupService.createGroup(
                userService.getUserBySession(), request)
        );
    }

    @GetMapping
    Response<UserGroupDto> getGroups(){
        return ResponseHelper.isOk(
                groupService.getGroupData(userService.getUserBySession())
        );
    }

    @PostMapping("/{groupId}/chat")
    Response<Chat> addGroupPost(@PathVariable("groupId") String groupId,
                                @RequestBody ChatRequest request){
        return ResponseHelper.isOk(
                groupService.addChat(userService.getUserBySession(), groupId, request)
        );
    }

    @GetMapping("/{groupId}/chat")
    Response<List<Chat>> getGroupChats(
            @PathVariable("groupId") String groupId,
            @RequestParam("fromTime") Long milliseconds){

        Date fromDate = new Date(milliseconds);
        return ResponseHelper.isOk(
                groupService.getGroupChat(userService.getUserBySession(), groupId, fromDate)
        );

    }

}
