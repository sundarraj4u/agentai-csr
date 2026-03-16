package com.csr.agent.service;

import com.csr.agent.agent.SupportAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class ChatOrchestrator {
    private static final Logger logger = LoggerFactory.getLogger(ChatOrchestrator.java);
    private final SupportAgent supportAgent;

    public ChatOrchestrator(SupportAgent supportAgent) {
        this.supportAgent = supportAgent;
    }

    /**
     * Executes the LLM flow. Retries automatically on unexpected network drops
     * or serialization anomalies from the LLM endpoint (exponential backoff).
     */
    @Retryable(
        retryFor = {Exception.class}, 
        maxAttempts = 3, 
        backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    public String processMessage(String sessionId, String userMessage) {
        return supportAgent.chat(sessionId, userMessage);
    }

    /**
     * Graceful Degradation: If retries exhaust, fallback here.
     */
    @Recover
    public String fallbackResponse(Exception e, String sessionId, String userMessage) {
        logger.error("FALLBACK_TRIGGERED: All retries failed for session: {}", sessionId, e);
        return "I'm currently experiencing technical difficulties connecting to my systems. " +
               "Please try again in a few moments or contact human support.";
    }
}
