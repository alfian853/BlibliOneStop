package com.gdn.onestop.service.impl;

import com.gdn.onestop.entity.User;
import com.gdn.onestop.repository.UserRepository;
import com.gdn.onestop.service.exception.InvalidRequestException;
import com.gdn.onestop.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class OsUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(userRepository.findByUsername(username))
            .orElseThrow(() -> new InvalidRequestException("Wrong username/password"));
    }
}
