package com.gdn.onestop.request;

import lombok.Data;

@Data
public class PostNoteRequest {
    Integer meetingNumber;
    Long lastUpdate;
    String note;
}
