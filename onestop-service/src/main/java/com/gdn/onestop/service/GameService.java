package com.gdn.onestop.service;


import com.gdn.onestop.entity.User;
import com.gdn.onestop.entity.UserGame;

public interface GameService {

    void onIdeationPosted(User user);
    void onIdeationCommented(User user);
    void onBookReadFinished(User user, String bookId);
    void onAudioListened(User user, String audioId);
    void onMeetingNoteEdited(User user);

    UserGame getOrCreate(User user);

    enum GamePoint {

        POST(10), COMMENT(1), READED_BOOK(10), LISTENED_AUDIO(10), WRITTEN_MEETING_NOTE(3);

        Integer point  = 0;

        GamePoint(Integer point){
            this.point = point;
        }

        public Integer getPoint(){
            return point;
        }
    }
}
