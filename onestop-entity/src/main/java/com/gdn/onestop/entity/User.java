package com.gdn.onestop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class User implements UserDetails, Principal {

    @Id
    String id;

    // auth
    String username;
    String password;

    Boolean isAdmin = false;

    String fcmToken = null;

    @Transient
    private boolean enabled = true;
    @Transient
    private boolean accountNonExpired = true;
    @Transient
    private boolean accountNonLocked = true;
    @Transient
    private boolean credentialsNonExpired = true;

    @Transient
    private Collection<? extends GrantedAuthority> ADMIN_AUTHORITIES
            = Collections.singletonList(new SimpleGrantedAuthority(ADMIN_CONST));

    @Transient
    private Collection<? extends GrantedAuthority> DEFAULT_AUTHORITIES = new LinkedList<>();

    @Transient
    private static String ADMIN_CONST = "ADMIN";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(isAdmin){
            return ADMIN_AUTHORITIES;
        }
        else return DEFAULT_AUTHORITIES;
    }

    @Override
    public String getName() {
        return username;
    }

}
