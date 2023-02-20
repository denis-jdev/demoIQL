package com.ds.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        final UserIdAuthenticationToken token = Optional.ofNullable(requestTokenHeader)
                .map(String::trim)
                .map(UserIdAuthenticationToken::new)
                .orElse(null);

        if (token != null && jwtTokenUtil.validateToken(token.getCredentials().toString())) {
            Long userId = jwtTokenUtil.getUserIdFromToken(requestTokenHeader);
            UserIdAuthenticationToken userIdAuthenticationToken = new UserIdAuthenticationToken(
                    userId, token.getCredentials().toString()
            );

            userIdAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(userIdAuthenticationToken);
        }

        chain.doFilter(request, response);
    }

}
