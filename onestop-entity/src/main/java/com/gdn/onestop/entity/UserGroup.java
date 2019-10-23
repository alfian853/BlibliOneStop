package com.gdn.onestop.entity;

import com.gdn.onestop.model.GroupType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document
public class UserGroup {


    // userId
    @Id
    String id;

    //save group Id
    List<String> guilds;
    List<String> squads;
    List<String> tribes;

    @Transient
    public List<String> getGroupByType(GroupType type){
        switch (type){
            case TRIBE:return tribes;
            case SQUAD:return squads;
            case GUILD:return guilds;
        }
        return null;
    }

    @LastModifiedDate
    Date updatedAt;

}
