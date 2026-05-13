package com.chatbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class LlmService {

    private final WebClient webClient;

    public LlmService(@Value("${groq.api.key}") String apiKey) {

        this.webClient = WebClient.builder()
                .baseUrl("https://api.groq.com/openai/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("x-groq-api-key", apiKey)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String ask(String userMessage) {

    	Map<String, Object> body = Map.of(
    	        "model", "openai/gpt-oss-120b",
    	        "messages", List.of(
    	                Map.of("role", "system", "content", "You are a helpful AI assistant."),
    	                Map.of("role", "user", "content", userMessage)
    	        ),
    	        "temperature", 0.7
    	       // "max_tokens", 300
    	);


        try {
            Map response = webClient.post()
                    .bodyValue(body)
                    .retrieve()
                    .onStatus(
                            status -> status.isError(),
                            clientResponse ->
                                    clientResponse.bodyToMono(String.class)
                                            .map(msg -> new RuntimeException("Groq FULL ERROR: " + msg))
                    )
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            System.out.println("AI RAW RESPONSE = " + response);

            List choices = (List) response.get("choices");
            Map first = (Map) choices.get(0);
            Map message = (Map) first.get("message");

            return message.get("content").toString();

        } catch (Exception e) {
            System.out.println("FULL ERROR DETAILS = " + e.getMessage());
            return "AI Error: " + e.getMessage();
        }
    }
}
