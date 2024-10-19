package com.codiary.backend.domain.member.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomMemberDetails implements UserDetails {

    private final String email;
    private final String password;
    private final Long id;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomMemberDetails(String email, Long id, String password, Collection<? extends GrantedAuthority> authorities) {
        this.email = email;
        this.id = id;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }
}
