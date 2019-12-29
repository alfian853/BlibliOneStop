package com.gdn.onestop.web.controller;

import com.gdn.onestop.entity.User;
import com.gdn.onestop.request.LoginRequest;
import com.gdn.onestop.request.UserRegisterRequest;
import com.gdn.onestop.response.LoginResponse;
import com.gdn.onestop.response.Response;
import com.gdn.onestop.response.ResponseHelper;
import com.gdn.onestop.service.UserService;
import com.gdn.onestop.service.exception.InvalidRequestException;
import com.gdn.onestop.web.config.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public Response<LoginResponse> login(@RequestBody LoginRequest data) {
        try {
            User user = (User) userService.loadUserByUsername(data.getUsername());

            String username = user.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));

            String token = jwtTokenProvider.createToken(username);

            LoginResponse response = new LoginResponse();
            response.setUsername(username);
            response.setToken(token);

            return ResponseHelper.isOk(response);
        } catch (AuthenticationException e) {
            throw new InvalidRequestException("Invalid username or password!");
        }
    }

    @PostMapping("/register")
    public Response<Boolean> register(@RequestBody UserRegisterRequest request){
        return ResponseHelper.isOk(
                userService.createUser(request)
        );
    }
}
