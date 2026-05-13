package com.chatbot.controller;

import com.chatbot.model.User;
import com.chatbot.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService service;

    @GetMapping("/register")
    public String registerPage(){

        return "register";
    }

    @PostMapping("/register")
    public String register(User user){

        service.registerUser(user);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(){

        return "login";
    }
}