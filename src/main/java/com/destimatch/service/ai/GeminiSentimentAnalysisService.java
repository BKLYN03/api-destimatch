package com.destimatch.service.ai;

import com.destimatch.service.ai.gemini.GeminiClient;
import com.destimatch.service.ai.gemini.GeminiRequest;
import com.destimatch.service.ai.gemini.GeminiResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class GeminiSentimentAnalysisService implements SentimentAnalysisService {

    @Inject @RestClient
    GeminiClient geminiClient;
    @Inject
    ObjectMapper objectMapper;
    @ConfigProperty(name = "gemini.api.key")
    String apiKey;

    @Override
    public AnalysisResult analyze(String text) {
        String prompt = """
            Analyse cet avis touristique : "%s"
            Identifie le sentiment (POSITIVE, NEGATIVE, NEUTRAL) pour ces aspects :
            - CLEANLINESS (Propreté)
            - VIBE (Ambiance/Bruit)
            - PRICE (Prix)
            
            Extrais aussi 2 mots-clés.
            Réponds UNIQUEMENT en JSON :
            { "aspects": {"CLEANLINESS": "...", "PRICE": "..."}, "keywords": ["...", "..."] }
            """.formatted(text);

        try {
            GeminiResponse response = geminiClient.generateContent(apiKey, GeminiRequest.of(prompt));
            
            String json = response.getText();
            if (json.contains("```json")) {
                json = json.replace("```json", "").replace("```", "");
            }
            json = json.trim();

            AiResponse mapped = objectMapper.readValue(json, AiResponse.class);
            return new AnalysisResult(mapped.aspects, mapped.keywords);

        } catch (Exception e) {
            System.err.println("ERREUR GEMINI : " + e.getMessage());
            return new AnalysisResult(new HashMap<>(), new ArrayList<>());
        }
    }

    @RegisterForReflection
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AiResponse {
        public Map<String, String> aspects = new HashMap<>();
        public List<String> keywords = new ArrayList<>();
    }
}
