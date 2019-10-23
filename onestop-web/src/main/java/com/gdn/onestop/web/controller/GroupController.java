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
