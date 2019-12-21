package com.gdn.onestop.service.impl;

import com.gdn.onestop.entity.User;
import com.gdn.onestop.entity.UserGame;
import com.gdn.onestop.repository.GameRepository;
import com.gdn.onestop.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    GameRepository gameRepository;

    @Override
    public void onIdeationPosted(User user) {
        UserGame userGame = getOrCreate(user);

        userGame.setIdeationPosts(userGame.getIdeationPosts()+1);
        userGame.increasePoints(GamePoint.POST.getPoint());

        gameRepository.save(userGame);
    }

    @Override
    public void onIdeationCommented(User user) {
        UserGame userGame = getOrCreate(user);

        userGame.setIdeationComments(userGame.getIdeationComments()+1);
        userGame.increasePoints(GamePoint.COMMENT.getPoint());

        gameRepository.save(userGame);
    }

    @Override
    public void onBookReadFinished(User user, String bookId) {
        UserGame userGame = getOrCreate(user);

        boolean isFinishedBefore = userGame.getFinishedAudiosId().contains(bookId);

        if(!isFinishedBefore){
            userGame.setReadedBooks(userGame.getReadedBooks()+1);
            userGame.increasePoints(GamePoint.READED_BOOK.getPoint());
            userGame.getFinishedBooksId().add(bookId);
            gameRepository.save(userGame);
        }

    }

    @Override
    public void onAudioListened(User user, String audioId) {
        UserGame userGame = getOrCreate(user);

        boolean isFinishedBefore = userGame.getFinishedBooksId().contains(audioId);

        if(!isFinishedBefore){
            userGame.setListenedAudios(userGame.getListenedAudios()+1);
            userGame.increasePoints(GamePoint.LISTENED_AUDIO.getPoint());
            userGame.getFinishedAudiosId().add(audioId);
            gameRepository.save(userGame);
        }

    }

    @Override
    public void onMeetingNoteEdited(User user) {
        UserGame userGame = getOrCreate(user);

        userGame.setWrittenMeetingNotes(userGame.getWrittenMeetingNotes()+1);
        userGame.increasePoints(GamePoint.WRITTEN_MEETING_NOTE.getPoint());

        gameRepository.save(userGame);

    }

    @Override
    public UserGame getOrCreate(User user) {
        return gameRepository.findById(user.getId()).orElse(new UserGame(user.getId()));
    }
}
