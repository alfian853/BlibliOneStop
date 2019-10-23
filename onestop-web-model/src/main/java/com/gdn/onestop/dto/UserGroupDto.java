package com.gdn.onestop.dto;

import com.gdn.onestop.model.GroupModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupDto {

    String username;

    Boolean withDetail = false;
    Date lastUpdate;
    List<GroupModel> guilds = new LinkedList<>();
    List<GroupModel> squads = new LinkedList<>();
    List<GroupModel> tribes = new LinkedList<>();

}
