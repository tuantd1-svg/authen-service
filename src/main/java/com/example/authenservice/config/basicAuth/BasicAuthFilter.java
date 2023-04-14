package com.example.authenservice.config.basicAuth;

import com.example.authenservice.service.BasicUserImp;
import com.example.authenservice.service.BasicUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@Component
public class BasicAuthFilter extends OncePerRequestFilter {

    @Autowired
    private BasicUserService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BASIC_AUTH_PREFIX = "Basic ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (header != null && header.startsWith(BASIC_AUTH_PREFIX)) {
            String credentials = new String(Base64.getDecoder().decode(header.substring(BASIC_AUTH_PREFIX.length())));
            String[] parts = credentials.split(":", 2);
            String username = parts[0];
            String password = parts[1];
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (userDetails != null && passwordEncoder.matches(password, userDetails.getPassword())) {
                if (userDetails instanceof BasicUserImp) {
                    BasicUserImp basicUserImp = (BasicUserImp) userDetails;
                    String url = request.getRequestURI();
                    if (!basicUserImp.getAllowFunction().contains(url) || !"*".equals(basicUserImp.getAllowFunction().get(0))) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                }
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
