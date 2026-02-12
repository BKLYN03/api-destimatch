package com.destimatch.service.ai;

import java.util.List;
import java.util.Map;

public interface SentimentAnalysisService {

    record AnalysisResult(Map<String, String> aspects, List<String> keywords) {}

    AnalysisResult analyze(String text);
}
