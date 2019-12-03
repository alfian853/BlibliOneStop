package com.gdn.onestop.response;

import lombok.Data;

@Data
public class MeetingNoteUpdateResponse {

    Boolean isConflict = false;
    String currentText = "";
    Long currentLastUpdate = 0L;
}