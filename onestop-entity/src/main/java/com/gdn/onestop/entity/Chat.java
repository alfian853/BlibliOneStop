package com.gdn.onestop.entity;


import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class Chat {

    String id;

    String username;

    String text;

    Date createdAt;

    Boolean isReply = false;
    String repliedId;
    String repliedText;

    Boolean isMeeting = false;
    Date meetingDate;

}
