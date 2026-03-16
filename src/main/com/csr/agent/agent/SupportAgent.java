
package com.csr.agent.agent;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface SupportAgent {
    @SystemMessage("""
        You are a helpful and secure customer support agent.
        Use the provided tools to fetch customer data when needed.
        If a tool fails or returns empty data, politely inform the user.
        Do NOT guess or hallucinate customer data.
    """)
    String chat(@MemoryId String sessionId, @UserMessage String userMessage);
}
