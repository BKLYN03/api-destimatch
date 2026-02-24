package com.destimatch.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class LocationValidationService {
    
    @ConfigProperty(name = "gemini.api.key")
    String apiKey;

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent";

    public boolean isValidLocation(String city, String country) {
        if (city == null || country == null)
            return false;

        String promptText = String.format(
            "Tu es un validateur géographique. Réponds UNIQUEMENT par 'TRUE' si %s est bien une ville située dans le pays %s, ou 'FALSE' sinon. Ne donne aucune explication.",
            city, country
        );

        String jsonBody = """
            {
              "contents": [{
                "parts": [{"text": "%s"}]
              }]
            }
            """.formatted(promptText.replace("\"", "\\\""));

        try (Client client = ClientBuilder.newClient()) {
            Response response = client.target(API_URL)
                    .queryParam("key", apiKey)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(jsonBody));

            if (response.getStatus() == 200) {
                String responseBody = response.readEntity(String.class);
                return responseBody.contains("TRUE");
            } else {
                System.out.println("Erreur Gemini: " + response.getStatus());
                return false; 
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
