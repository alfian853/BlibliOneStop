package com.gdn.onestop.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gdn.onestop.util.DateConverterUtil;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class MeetingModel {

    //from chat Id
    String id;

    Integer meetingNumber;

    String note;

    @JsonSerialize(converter = DateConverterUtil.class)
    Date lastUpdate;

    Date meetingDate;
}
