package com.chatbot.service;

import com.chatbot.model.User;
import com.chatbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    public void registerUser(User user){

        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("USER");

        repo.save(user);
    }
}