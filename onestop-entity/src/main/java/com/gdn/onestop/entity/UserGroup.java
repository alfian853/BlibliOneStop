package com.gdn.onestop.entity;

import com.gdn.onestop.model.GroupModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class UserGroup {


    // userId
    @Id
    String id;

    List<GroupModel> groups;
    List<GroupModel> squads;
    List<GroupModel> tribes;

}
