package com.cydeo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Configuration
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if(roles.contains("Admin"))response.sendRedirect("/dashboards/admin");
        if(roles.contains("Instructor"))response.sendRedirect("/dashboards/instructor");
        if(roles.contains("Cydeo Mentor")) response.sendRedirect("/dashboards/cydeoMentor");
        if(roles.contains("Alumni Mentor")) response.sendRedirect("/dashboards/alumniMentor");
        if(roles.contains("Student")) response.sendRedirect("/dashboards/student");
    }

}
