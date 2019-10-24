package com.gdn.onestop.request;

import lombok.Data;
import java.util.Date;

@Data
public class ChatSendRequest {
    String groupId;

    String text;

    Boolean isReply = false;
    String repliedText;
    String repliedId;

    Boolean isMeeting = false;
    Date meetingDate;
}
