package com.chatbot.service;

import com.chatbot.model.ChatMessage;
import com.chatbot.repository.ChatMessageRepository;

import org.springframework.stereotype.Service;

import edu.stanford.nlp.pipeline.*;

import java.util.List;
import java.util.Properties;

@Service
public class ChatService {

    private final ChatMessageRepository repo;
    private StanfordCoreNLP pipeline;

    public ChatService(ChatMessageRepository repo) {
        this.repo = repo;

        // Initialize NLP pipeline
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma");

        pipeline = new StanfordCoreNLP(props);
    }

    // Process message using NLP
    public String processMessage(String message) {

        CoreDocument document = new CoreDocument(message);
        pipeline.annotate(document);

        StringBuilder processedText = new StringBuilder();

        document.tokens().forEach(token -> {
            processedText.append(token.lemma()).append(" ");
        });

        return processedText.toString().toLowerCase();
    }

    // Chatbot response logic
    public String getBotResponse(String userMessage) {

        String processed = processMessage(userMessage);

        if (processed.contains("admission")) {
            return "Admissions start in June. You can apply online.";
        }

        if (processed.contains("fee")) {
            return "The college fee is approximately 50,000 per year.";
        }

        if (processed.contains("course")) {
            return "Our college offers B.Tech, MBA, and MCA courses.";
        }

        if (processed.contains("hostel")) {
            return "Yes, hostel facilities are available for boys and girls.";
        }

        if (processed.contains("contact")) {
            return "You can contact the college at +91-9876543210.";
        }

        return "Sorry, I didn't understand your question. Please try asking about admissions, fees, courses, or hostel.";
    }

    // Save message for a specific user
    public ChatMessage save(String userEmail, String role, String message) {

        ChatMessage m = new ChatMessage(userEmail, role, message);
        return repo.save(m);
    }

    // Load history for logged-in user
    public List<ChatMessage> loadHistory(String userEmail) {

        return repo.findTop100ByUserEmailOrderByIdAsc(userEmail);
    }

    // Clear history only for logged-in user
    public void clearHistory(String userEmail) {

        List<ChatMessage> messages = repo.findTop100ByUserEmailOrderByIdAsc(userEmail);
        repo.deleteAll(messages);
    }
}