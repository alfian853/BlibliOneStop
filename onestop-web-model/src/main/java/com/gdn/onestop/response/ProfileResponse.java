package com.gdn.onestop.response;

import com.gdn.onestop.dto.IdeaPostDto;
import lombok.Data;

import java.util.List;

@Data
public class ProfileResponse {

    Integer ideationPosts = 0;
    Integer ideationComments = 0;
    Integer readedBooks = 0;
    Integer listenedAudios = 0;
    Integer writtenMeetingNotes = 0;
    Integer points = 0;

    List<IdeaPostDto> topIdeas;
}
