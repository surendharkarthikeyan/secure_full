package com.secure_ai_chat.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AiService {

    @Value("${ollama.api.url}")
    private String ollamaApiUrl;

    @Value("${ollama.model}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getResponse(String safeText) {

        try {
            // 1️⃣ Build Ollama request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("prompt", safeText);
            requestBody.put("stream", false);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> requestEntity =
                    new HttpEntity<>(requestBody, headers);

            // 2️⃣ Call Ollama
            ResponseEntity<String> responseEntity =
                    restTemplate.postForEntity(
                            ollamaApiUrl,
                            requestEntity,
                            String.class
                    );

            // 3️⃣ Parse response
            JsonNode root = objectMapper.readTree(responseEntity.getBody());

            return root.path("response").asText();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error communicating with local AI model.";
        }
    }
}
