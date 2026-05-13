package com.chatbot.repository;

import com.chatbot.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Load last 100 messages for a specific user (oldest first)
    List<ChatMessage> findTop100ByUserEmailOrderByIdAsc(String userEmail);

}