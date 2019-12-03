package com.gdn.onestop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gdn.onestop.model.MeetingModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;
import java.util.List;

@Data
@Document
public class GroupMeeting {

    @Id
    String id;

    List<MeetingModel> meetings;

    @Transient
    public List<MeetingModel> getOrCreateMeetingList(){
        if(meetings == null){
            meetings = new LinkedList<>();
        }
        return meetings;
    }
}
