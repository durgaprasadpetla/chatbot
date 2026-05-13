package com.chatbot.controller;

import com.chatbot.model.ChatMessage;
import com.chatbot.service.ChatService;
import com.chatbot.service.LlmService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final LlmService llmService;
    private final ChatService chatService;

    public ChatController(LlmService llmService, ChatService chatService) {
        this.llmService = llmService;
        this.chatService = chatService;
    }

    // Request body
    record ChatRequest(String message) {}

    // Response body
    record ChatResponse(String reply) {}

    // -----------------------------------------
    // SEND MESSAGE + SAVE TO DATABASE
    // -----------------------------------------
    @PostMapping("/send")
    public ResponseEntity<ChatResponse> send(@RequestBody ChatRequest request,
                                             Authentication auth) {

        // Logged-in user email
        String userEmail = auth.getName();

        // 1️⃣ Save user message
        chatService.save(userEmail, "user", request.message());

        // 2️⃣ Get AI reply
        String aiReply = llmService.ask(request.message());

        // 3️⃣ Save bot reply
        chatService.save(userEmail, "bot", aiReply);

        // 4️⃣ Send reply to frontend
        return ResponseEntity.ok(new ChatResponse(aiReply));
    }

    // -----------------------------------------
    // GET CHAT HISTORY (ONLY FOR LOGGED USER)
    // -----------------------------------------
    @GetMapping("/history")
    public List<ChatMessage> getHistory(Authentication auth) {

        String userEmail = auth.getName();

        return chatService.loadHistory(userEmail);
    }

    // -----------------------------------------
    // CLEAR HISTORY (ONLY FOR LOGGED USER)
    // -----------------------------------------
    @DeleteMapping("/history/clear")
    public ResponseEntity<String> clear(Authentication auth) {

        String userEmail = auth.getName();

        chatService.clearHistory(userEmail);

        return ResponseEntity.ok("Chat history cleared!");
    }
}