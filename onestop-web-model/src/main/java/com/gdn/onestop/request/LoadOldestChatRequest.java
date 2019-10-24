package com.gdn.onestop.request;

import lombok.Data;

@Data
public class LoadOldestChatRequest {

    String groupId;
    Long beforeTime;

}
