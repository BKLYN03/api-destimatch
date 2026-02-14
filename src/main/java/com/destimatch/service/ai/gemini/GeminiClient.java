package com.destimatch.service.ai.gemini;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "https://generativelanguage.googleapis.com")
public interface GeminiClient {

    @POST
    @Path("/v1beta/models/gemini-flash-latest:generateContent")
    GeminiResponse generateContent(@QueryParam("key") String apiKey, GeminiRequest request);
}