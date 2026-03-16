# AI Enterprise Agent (Spring Boot + LangChain4j + Azure)

This repository contains a secure, highly-observable AI Agent utilizing the LangChain4j framework. 

## Capabilities
* **Stateful Memory**: Utilizes `MessageWindowChatMemory` mapped to unique session IDs.
* **Function Calling**: LLM detects when external data is needed and securely invokes internal Java methods (`@Tool`).
* **Azure Key Vault**: Credentials are fetched dynamically using Azure Managed Identity (Zero password rotation required).
* **Observability**: Azure Application Insights tracks custom metrics including Token Usage, latency, and Tool input payloads.
* **Resilience**: Implements Spring `@Retryable` with exponential backoff and `@Recover` for graceful degradation against non-deterministic LLM behavior.

## Pre-Requisites
1. Java 21 LTS installed.
2. Maven 3.8+ installed.
3. Azure CLI (`az login` with access to the KeyVault).

## Local Environment Configuration
Because the app relies on Azure Managed Identity, log in to your Azure CLI before running locally:
```bash
az login

# Build the project
mvn clean install

# Run the project
mvn spring-boot:run

Testing the agent
# 1. Ask a normal question
curl -X POST http://localhost:8080/api/chat/session-123 \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello! What can you do?"}'

# 2. Trigger the Database Tool
curl -X POST http://localhost:8080/api/chat/session-123 \
  -H "Content-Type: application/json" \
  -d '{"message": "Can you check the balance for john@example.com?"}'


git init
git remote add origin git@gitlab.com:your-company/ai-agent-service.git
git add .
git commit -m "feat: Initial commit of Spring Boot Langchain4j Agent"
git push -u origin main
