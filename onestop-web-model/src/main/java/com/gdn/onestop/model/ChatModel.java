package com.gdn.onestop.model;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gdn.onestop.util.DateConverterUtil;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class ChatModel {

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

}
