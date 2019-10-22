package com.gdn.onestop.service;

import com.gdn.onestop.entity.User;
import com.gdn.onestop.request.UserRegisterRequest;

public interface UserService {

    User getUserBySession();
    boolean createUser(UserRegisterRequest request);
}
