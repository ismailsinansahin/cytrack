package com.cydeo.config;

import com.cydeo.service.SecurityService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityService securityService;
    private final AuthSuccessHandler authSuccessHandler;

    public SecurityConfig(SecurityService securityService, AuthSuccessHandler authSuccessHandler) {
        this.securityService = securityService;
        this.authSuccessHandler = authSuccessHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()      //All requests should be authorized
                .antMatchers("/", "/login/**", "/fragments/**", "/assets/**", "/images/**").permitAll()
                .antMatchers("/dashboard/**").hasAnyAuthority("Admin","Instructor","Cydeo Mentor", "Alumni Mentor", "Student")
                .antMatchers("/user/**").hasAnyAuthority("Admin")
                .antMatchers("/batch/**").hasAnyAuthority("Admin")
                .antMatchers("/lesson/**").hasAnyAuthority("Admin")
                .antMatchers("/group/**").hasAnyAuthority("Admin")
                .antMatchers("/task/**").hasAnyAuthority("Admin","Instructor", "Cydeo Mentor")
            .and()
            .formLogin()
                .loginPage("/login")
                .successHandler(authSuccessHandler)
                .failureUrl("/login?error=true")
                .permitAll()
            .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
            .and()
            .rememberMe()
                .tokenValiditySeconds(120)
                .key("cytrack")
                .userDetailsService(securityService);
    }

}
