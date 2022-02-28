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
                .antMatchers("/users/**").hasAnyAuthority("Admin")
                .antMatchers("/batches/**").hasAnyAuthority("Admin")
                .antMatchers("/lessons/**").hasAnyAuthority("Admin")
                .antMatchers("/groups/**").hasAnyAuthority("Admin")
                .antMatchers("/tasks/**").hasAnyAuthority("Admin","Instructor", "Cydeo Mentor")
                .antMatchers("/dashboards/**").hasAnyAuthority("Admin","Instructor","Cydeo Mentor", "Alumni Mentor", "Student")
//                .antMatchers("/statistics/**").hasAnyAuthority("Admin")
                .antMatchers(             //All public permits are declared here
                    "/",
                    "/login/**",
                    "/fragments/**",  //Otherwise our page does not look fancy
                    "/assets/**",
                    "/images/**")
                .permitAll()          //Give the access permit to all user types for these pages
            .and()
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/welcome")     //Successful login mapping for all users
//                .successHandler(authSuccessHandler)  //Delegate login mapping to AuthSuccessHandler class
//                .failureUrl("login?error=true")      //Unsuccessful login mapping
                .permitAll()                         //They are same for all user types
            .and()
            .rememberMe()
                .tokenValiditySeconds(120)
                .key("cytrack")
                .userDetailsService(securityService)
            .and()
            .logout()                                //fix logout code
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login");
    }
}
