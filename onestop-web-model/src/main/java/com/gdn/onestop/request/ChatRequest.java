package com.gdn.onestop.request;

import lombok.Data;

import java.util.Date;

@Data
public class ChatRequest {
    String text;
    Boolean isReply = false;
    String repliedText;
    String repliedId;

    Boolean isMeeting = false;
    Date meetingDate;
}
