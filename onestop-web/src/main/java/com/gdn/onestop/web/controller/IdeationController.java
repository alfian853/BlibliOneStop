package com.gdn.onestop.web.controller;

import com.gdn.onestop.dto.IdeaPostDto;
import com.gdn.onestop.dto.PostDto;
import com.gdn.onestop.entity.User;
import com.gdn.onestop.repository.UserRepository;
import com.gdn.onestop.request.IdeationRequest;
import com.gdn.onestop.response.Response;
import com.gdn.onestop.response.ResponseHelper;
import com.gdn.onestop.service.IdeationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/idea")
public class IdeationController {

    @Autowired
    IdeationService ideationService;

    @Autowired
    UserRepository userRepository;

    @PostMapping
    public Response<PostDto> postIdea(@Valid @RequestBody IdeationRequest request){
        ideationService.addIdeation(request);
        return Response.<PostDto>builder().status("success").code(200).data(new PostDto()).build();
    }

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping
    public Response<List<IdeaPostDto>> getIdeas(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(value = "item_per_page", defaultValue = "5") Integer itemPerPage){

        return ResponseHelper.isOk(
                ideationService.getIdeations(page, itemPerPage)
        );
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
