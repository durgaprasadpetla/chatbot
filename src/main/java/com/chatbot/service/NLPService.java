package com.chatbot.service;

import java.util.Properties;
import org.springframework.stereotype.Service;

import edu.stanford.nlp.pipeline.*;

@Service
public class NLPService {

    private StanfordCoreNLP pipeline;

    public NLPService() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma");

        pipeline = new StanfordCoreNLP(props);
    }

    public String processMessage(String message) {

        CoreDocument document = new CoreDocument(message);
        pipeline.annotate(document);

        StringBuilder processed = new StringBuilder();

        document.tokens().forEach(token -> {
            processed.append(token.lemma()).append(" ");
        });

        return processed.toString();
    }
}