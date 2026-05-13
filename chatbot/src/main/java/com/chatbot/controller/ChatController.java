package com.chatbot.controller;

import com.chatbot.model.ChatMessage;
import com.chatbot.service.ChatService;
import com.chatbot.service.LlmService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ChatResponse> send(@RequestBody ChatRequest request) {

        // 1️⃣ Save user message
        chatService.save("user", request.message());

        // 2️⃣ Get AI reply
        String aiReply = llmService.ask(request.message());

        // 3️⃣ Save bot reply
        chatService.save("bot", aiReply);

        // 4️⃣ Send reply to frontend
        return ResponseEntity.ok(new ChatResponse(aiReply));
    }

    // -----------------------------------------
    // GET CHAT HISTORY
    // -----------------------------------------
    @GetMapping("/history")
    public List<ChatMessage> getHistory() {
        return chatService.loadHistory();
    }

    // -----------------------------------------
    // CLEAR HISTORY
    // -----------------------------------------
    @DeleteMapping("/history/clear")
    public ResponseEntity<String> clear() {
        chatService.clearHistory();
        return ResponseEntity.ok("Chat history cleared!");
    }
}
