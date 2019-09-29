package com.gdn.onestop.web.controller;

import com.gdn.onestop.entity.User;
import com.gdn.onestop.request.LoginRequest;
import com.gdn.onestop.response.LoginResponse;
import com.gdn.onestop.service.impl.MongoUserDetailsService;
import com.gdn.onestop.web.config.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
    MongoUserDetailsService userService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest data) {
        User user = userService.loadUserByUsername(data.getUsername());

        try {
            String username = user.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));

            String token = jwtTokenProvider.createToken(username);

            LoginResponse response = new LoginResponse();
            response.setUsername(username);
            response.setToken(token);

            return response;
        } catch (AuthenticationException e) {
            throw new BadCredentialsException ("Invalid username or password!");
        }
    }
}
