package com.ds.demo.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class UserIdAuthenticationToken extends AbstractAuthenticationToken {
    private Long userId;
    private String token;

    public UserIdAuthenticationToken(Long userId, String token) {
        super(Collections.emptyList());
        this.userId = userId;
        this.token = token;
        this.setAuthenticated(true);
    }

    public UserIdAuthenticationToken(String token) {
        super(Collections.emptyList());
        this.token = token;
        this.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.token = null;
    }
}
