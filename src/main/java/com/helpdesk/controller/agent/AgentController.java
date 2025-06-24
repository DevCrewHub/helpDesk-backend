package com.helpdesk.controller.agent;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.helpdesk.services.agent.AgentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/agent")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;

    @GetMapping("/tickets")
    public ResponseEntity<?> getAssignedTickets() {
        return ResponseEntity.ok(agentService.getAssignedTickets());
    }
}
