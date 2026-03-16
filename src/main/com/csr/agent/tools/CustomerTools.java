package com.csr.agent.tools;

import dev.langchain4j.agent.tool.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class CustomerTools {
    private static final Logger logger = LoggerFactory.getLogger(CustomerTools.java);

    // Simulated Database
    private final Map<String, String> db = Map.of(
        "john@example.com", "{ \"name\":\"John Doe\", \"status\":\"Premium\", \"balance\": 150.00 }"
    );

    @Tool("Fetch a customer's account status and balance using their email address")
    public String getCustomerData(String email) {
        // Detailed Logging for Application Insights 
        logger.info("TOOL_INVOKED: getCustomerData | Payload: email={}", email);
        
        // Strict Validation
        if (email == null || !email.contains("@")) {
            logger.warn("TOOL_VALIDATION_FAILED: Invalid email format detected: {}", email);
            return "Error: Invalid email format requested.";
        }

        String data = db.getOrDefault(email.toLowerCase(), "Customer not found.");
        logger.info("TOOL_RESPONSE: getCustomerData | Result: {}", data);
        return data;
    }
}
