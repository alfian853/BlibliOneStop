package com.gdn.onestop.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupModel {

    String id;
    String name;

    String groupCode;

    //nullable for DB document and mandatory for single data response
    GroupType type;
 }
