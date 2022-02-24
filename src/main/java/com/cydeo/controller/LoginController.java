package com.cydeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping(value = {"/","/login"})
    public String goLoginPage(){
        return "login";
    }

    @PostMapping("/welcome")
    public String goWelcomePage(){
        return "welcome";
    }

}
