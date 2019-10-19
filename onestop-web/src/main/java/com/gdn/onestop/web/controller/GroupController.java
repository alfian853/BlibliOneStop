package com.gdn.onestop.web.controller;

import com.gdn.onestop.dto.UserGroupDto;
import com.gdn.onestop.model.GroupModel;
import com.gdn.onestop.request.CreateGroupRequest;
import com.gdn.onestop.response.Response;
import com.gdn.onestop.response.ResponseHelper;
import com.gdn.onestop.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group")
public class GroupController {


    @Autowired
    GroupService groupService;

    @PostMapping
    Response<GroupModel> createGroup(@RequestBody CreateGroupRequest request){
        return ResponseHelper.isOk(groupService.createGroup(request));
    }

    @GetMapping
    Response<UserGroupDto> getGroups(){
        return ResponseHelper.isOk(groupService.getGroupData());
    }
}
