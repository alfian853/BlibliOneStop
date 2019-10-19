package com.gdn.onestop.service;

import com.gdn.onestop.model.GroupModel;
import com.gdn.onestop.dto.UserGroupDto;
import com.gdn.onestop.request.CreateGroupRequest;

public interface GroupService {

    GroupModel createGroup(CreateGroupRequest request);
    UserGroupDto getGroupData();
}
