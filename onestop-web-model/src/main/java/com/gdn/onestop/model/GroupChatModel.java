package com.gdn.onestop.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gdn.onestop.util.DateConverterUtil;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupChatModel {

    String id;

    String username;

    String text;

    @JsonSerialize(converter = DateConverterUtil.class)
    Date createdAt;

    Boolean isReply = false;
    String repliedId;
    String repliedText;
    String repliedUsername;

    Boolean isMeeting = false;

    @JsonSerialize(converter = DateConverterUtil.class)
    Date meetingDate;

    Integer meetingNo;

}
