package com.gdn.onestop.service.impl;

import com.gdn.onestop.entity.User;
import com.gdn.onestop.repository.UserRepository;
import com.gdn.onestop.request.UserRegisterRequest;
import com.gdn.onestop.service.UserService;
import com.gdn.onestop.service.exception.InvalidRequestException;
import org.omg.CORBA.DynAnyPackage.Invalid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserBySession() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
//        User user = new User();
//        user.setUsername("user");
//        user.setId("5d78f00d1293690d6d2dd5ee");

//        return user;
    }

    @Override
    public boolean createUser(UserRegisterRequest request) {

        if(!request.getPassword().equals(request.getMatchPassword())){
            throw new InvalidRequestException("password doesn\'t match!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return true;
    }
}
