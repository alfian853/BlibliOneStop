package com.gdn.onestop.dto;

import com.gdn.onestop.model.GroupModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserGroupDto {

    String username;
    List<GroupModel> groups;
    List<GroupModel> squads;
    List<GroupModel> tribes;

}
