package com.gdn.onestop.web.controller;

import com.gdn.onestop.dto.CommentDto;
import com.gdn.onestop.dto.IdeaPostDto;
import com.gdn.onestop.entity.IdeaComment;
import com.gdn.onestop.entity.IdeaPost;
import com.gdn.onestop.entity.User;
import com.gdn.onestop.repository.UserRepository;
import com.gdn.onestop.request.CommentRequest;
import com.gdn.onestop.request.IdeationRequest;
import com.gdn.onestop.response.CommentPostResponse;
import com.gdn.onestop.response.Response;
import com.gdn.onestop.response.ResponseHelper;
import com.gdn.onestop.service.IdeationService;
import com.gdn.onestop.service.exception.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/idea")
public class IdeationController {

    @Autowired
    IdeationService ideationService;

    @Autowired
    UserRepository userRepository;

    @PostMapping
    public Response<IdeaPostDto> postIdea(@Valid @RequestBody IdeationRequest request){
        return Response.<IdeaPostDto>builder()
                .status("success")
                .code(200)
                .data(ideationService.addIdea(request))
                .build();
    }

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping
    public Response<List<IdeaPostDto>> getIdeas(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(value = "item_per_page", defaultValue = "5") Integer itemPerPage){
            if(page < 1)throw new InvalidRequestException("min page is 1");
            page--;
        return ResponseHelper.isOk(
                ideationService.getIdeas(page, itemPerPage)
        );
    }


    @PostMapping("/{id}/vote")
    public Response<Boolean> voteIdea(@PathVariable("id") String id, @RequestParam("vote_up") Boolean isVoteUp){
        return ResponseHelper.isOk(ideationService.voteIdea(id, isVoteUp));
    }

    @GetMapping("/{id}/vote")
    public Response<Map<String, Boolean>> getIdeaVoter(@PathVariable("id") String id){
        return ResponseHelper.isOk(ideationService.getIdeaVoter(id));
    }

    @PostMapping("/{id}/comment")
    public Response<CommentDto> postComment(
            @PathVariable("id") String id,
            @Valid @RequestBody CommentRequest request){
        return ResponseHelper.isOk(
                ideationService.addComment(id, request.getText())
        );
    }

    @GetMapping("/{id}/comment")
    public Response<List<IdeaComment.CommentUnit>> getComments(
            @PathVariable("id") String id,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(value = "item_per_page", defaultValue = "5") Integer itemPerPage){

        return ResponseHelper.isOk(ideationService.getComments(id, page, itemPerPage));
    }

    @PostMapping("/create-user")
    public String createuser(){
        User user = User.builder()
                .isAdmin(true)
                .level(0)
                .likesFromComment(0)
                .likesFromPosting(0)
                .password(passwordEncoder.encode("wkwkwk"))
                .username("user")
                .listenedAudios(0)
                .readedBooks(0)
                .build();

        userRepository.save(user);
        return "hehe";
    }




}
