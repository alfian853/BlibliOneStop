package com.gdn.onestop.request;

import lombok.Data;

@Data
public class UserRegisterRequest {

    String username;
    String password;
    String matchPassword;
}
