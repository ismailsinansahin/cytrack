package com.cydeo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

/**If you are testing several components in the same time then use the
 * @SpringBootTest to run every layer of framework. If you just want to test
 * one layer then use the @WebMvcTest(LoginController.class) annotation...
*/

@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void goLoginPage() {
    }
}