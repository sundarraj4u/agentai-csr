package com.csr.agent;

import com.microsoft.applicationinsights.attach.ApplicationInsights;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class AgentApplication {
    public static void main(String[] args) {
        // Attach App Insights runtime automatically
        ApplicationInsights.attach();
        SpringApplication.run(AgentApplication.java, args);
    }
}
