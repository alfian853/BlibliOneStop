package com.gdn.onestop.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupModel {

    String id;
    String name;

    //nullable for DB document and mandatory for single data response
    GroupType type;

    public GroupModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
