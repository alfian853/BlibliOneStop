package com.gdn.onestop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;
import java.util.List;

@Data
@Document
public class UserGame {

    public UserGame(String id) {
        this.id = id;
    }

    @Id
    String id;

    // game
    Integer ideationPosts = 0;
    Integer ideationComments = 0;
    Integer readedBooks = 0;
    Integer listenedAudios = 0;
    Integer writtenMeetingNotes = 0;
    Integer points = 0;

    List<String> finishedBooksId = new LinkedList<>();
    List<String> finishedAudiosId = new LinkedList<>();


    @JsonIgnore
    public void increasePoints(Integer points){
        this.points += points;
    }
}
