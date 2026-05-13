package com.chatbot.service;

import com.chatbot.model.ChatMessage;
import com.chatbot.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    private final ChatMessageRepository repo;

    public ChatService(ChatMessageRepository repo) {
        this.repo = repo;
    }

    public ChatMessage save(String role, String message) {
        ChatMessage m = new ChatMessage(role, message);
        return repo.save(m);
    }

    public List<ChatMessage> loadHistory() {
        return repo.findTop100ByOrderByIdAsc();
    }

    public void clearHistory() {
        repo.deleteAll();
    }
}
