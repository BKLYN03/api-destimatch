package com.destimatch.service.ai.gemini;

import java.util.List;

public record GeminiResponse(List<Candidate> candidates) {

    public record Candidate(Content content) {}
    public record Content(List<Part> parts) {}
    public record Part(String text) {}

    public String getText() {
        if (candidates != null && !candidates.isEmpty())
            return candidates.getFirst().content().parts().getFirst().text();
        return "{}";
    }
}
