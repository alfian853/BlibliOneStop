package com.gdn.onestop.service;

import com.gdn.onestop.entity.User;
import com.gdn.onestop.request.UserRegisterRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User getUserBySession();
    boolean createUser(UserRegisterRequest request);
}
