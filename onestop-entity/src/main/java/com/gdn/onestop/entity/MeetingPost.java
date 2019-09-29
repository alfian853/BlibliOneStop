package com.gdn.onestop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("meeting_posts")
public class MeetingPost extends Post {

    String title;
    String place;
    Date meetingDate;
}
