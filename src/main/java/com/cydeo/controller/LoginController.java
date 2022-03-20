package com.cydeo.controller;

import com.cydeo.annotations.ExecutionTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@ExecutionTime
public class LoginController {

    @GetMapping(value = {"/","/login"})
    public String goLoginPage(){
        return "login";
    }

}
