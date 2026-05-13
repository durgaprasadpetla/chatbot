package com.chatbot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/chat")
    public String chatPage() {
        return "chat";
    }

    @GetMapping("/history")
    public String historyPage() {
        return "history";
    }
}
