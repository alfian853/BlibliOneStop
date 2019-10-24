package com.gdn.onestop.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonSerialize
public class GroupChatRequest {

    String groupId;
    Date fromTime;
}
