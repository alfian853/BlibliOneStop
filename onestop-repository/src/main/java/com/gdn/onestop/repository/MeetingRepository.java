package com.gdn.onestop.repository;


import com.gdn.onestop.entity.GroupMeeting;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MeetingRepository extends MongoRepository<GroupMeeting, String> {

    GroupMeeting findGroupMeetingById(String id);
}
