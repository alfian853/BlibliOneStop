package com.gdn.onestop.web.controller;

import com.gdn.onestop.entity.User;
import com.gdn.onestop.entity.UserGame;
import com.gdn.onestop.response.ProfileResponse;
import com.gdn.onestop.response.Response;
import com.gdn.onestop.response.ResponseHelper;
import com.gdn.onestop.service.GameService;
import com.gdn.onestop.service.IdeationService;
import com.gdn.onestop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class UserController {

    @Autowired
    IdeationService ideationService;

    @Autowired
    GameService gameService;

    @Autowired
    UserService userService;

    @GetMapping("/{username}")
    public Response<ProfileResponse> getProfile(@PathVariable("username") String username){

        ProfileResponse response = new ProfileResponse();
        UserGame userGame = gameService.getOrCreate((User) userService.loadUserByUsername(username));

        response.setId(userGame.getId());
        response.setIdeationComments(userGame.getIdeationComments());
        response.setIdeationPosts(userGame.getIdeationPosts());
        response.setListenedAudios(userGame.getListenedAudios());
        response.setReadedBooks(userGame.getReadedBooks());
        response.setPoints(userGame.getPoints());
        response.setWrittenMeetingNotes(userGame.getWrittenMeetingNotes());

        response.setTopIdeas(
            ideationService.getTopPostByUsername(userService.getUserBySession(), username)
        );

        return ResponseHelper.isOk(response);
    }
}
