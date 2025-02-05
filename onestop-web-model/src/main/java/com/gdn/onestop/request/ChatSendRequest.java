package com.gdn.onestop.request;

import lombok.Data;

import java.util.Date;

@Data
public class ChatSendRequest {

    String text;

    Boolean isReply = false;
    String repliedText;
    String repliedId;
    String repliedUsername;
}
