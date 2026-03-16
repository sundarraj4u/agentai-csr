package com.example.agent.controller;

import com.example.agent.service.ChatOrchestrator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class AgentController {

    private final ChatOrchestrator chatOrchestrator;

    public AgentController(ChatOrchestrator chatOrchestrator) {
        this.chatOrchestrator = chatOrchestrator;
    }

    @PostMapping("/{sessionId}")
    public ResponseEntity<Map<String, String>> chat(
            @PathVariable String sessionId,
            @RequestBody Map<String, String> request) {
        
        String userMessage = request.get("message");
        String response = chatOrchestrator.processMessage(sessionId, userMessage);
        
        return ResponseEntity.ok(Map.of(
                "sessionId", sessionId,
                "reply", response
        ));
    }
}
