package com.gdn.onestop.request;

import com.gdn.onestop.model.GroupType;
import lombok.Data;

@Data
public class CreateGroupRequest {

    String name;
    GroupType type;
}
