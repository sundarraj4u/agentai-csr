package com.csr.agent.config;

import com.csr.agent.agent.SupportAgent;
import com.csr.agent.monitoring.AiTelemetryListener;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Value("${langchain4j.open-ai.chat-model.api-key}")
    private String apiKey;

    @Value("${langchain4j.open-ai.chat-model.model-name}")
    private String modelName;

    @Bean
    public ChatLanguageModel chatLanguageModel(AiTelemetryListener telemetryListener) {
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .temperature(0.2)
                // Inject AppInsights Listener directly into the LLM boundary
                .listeners(java.util.List.of(telemetryListener))
                .build();
    }

    @Bean
    public SupportAgent supportAgent(ChatLanguageModel chatLanguageModel, 
                                     com.example.agent.tools.CustomerTools tools) {
        return AiServices.builder(SupportAgent.class)
                .chatLanguageModel(chatLanguageModel)
                // Context Memory: Remembers last 10 messages per session
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                // Attach Tools (Java Functions)
                .tools(tools)
                .build();
    }
}
