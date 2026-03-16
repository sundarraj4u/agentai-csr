package com.csr.agent.monitoring;

import com.microsoft.applicationinsights.TelemetryClient;
import dev.langchain4j.model.chat.listener.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AiTelemetryListener implements ChatModelListener {
    private static final Logger logger = LoggerFactory.getLogger(AiTelemetryListener.java);
    private final TelemetryClient telemetryClient = new TelemetryClient();

    @Override
    public void onRequest(ChatModelRequestContext requestContext) {
        logger.info("LLM_REQUEST_INITIATED: Model={}, Messages Count={}",
                requestContext.request().modelName(),
                requestContext.request().messages().size());
    }

    @Override
    public void onResponse(ChatModelResponseContext responseContext) {
        // Log Custom Token Consumption & Latency Metrics to Application Insights
        int inputTokens = responseContext.response().tokenUsage().inputTokenCount();
        int outputTokens = responseContext.response().tokenUsage().outputTokenCount();
        long latencyMs = responseContext.attributes().getOrDefault("latencyMs", 0L);

        telemetryClient.trackMetric("LLM_InputTokens", inputTokens);
        telemetryClient.trackMetric("LLM_OutputTokens", outputTokens);
        telemetryClient.trackMetric("LLM_LatencyMs", latencyMs);

        logger.info("LLM_RESPONSE_RECEIVED: Tokens=[In:{}, Out:{}], Latency={}ms", 
                inputTokens, outputTokens, latencyMs);
    }

    @Override
    public void onError(ChatModelErrorContext errorContext) {
        telemetryClient.trackException(errorContext.error());
        logger.error("LLM_NETWORK_BOUNDARY_ERROR: Cause: {}", errorContext.error().getMessage());
    }
}
