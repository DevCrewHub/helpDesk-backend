package com.helpdesk.controller.agent;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.helpdesk.dto.TicketDto;
import com.helpdesk.enums.TicketStatus;
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
    
    @PutMapping("/tickets/{ticketId}/status")
    public ResponseEntity<?> updateTicketStatus(@PathVariable Long ticketId, @RequestParam String status) {
        try {
            TicketStatus newStatus = TicketStatus.valueOf(status.toUpperCase());
            TicketDto updatedTicket = agentService.updateTicketStatus(ticketId, newStatus);
            return ResponseEntity.ok(updatedTicket);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status: " + status);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/tickets/search/{title}")
    public ResponseEntity<List<TicketDto>> searchTask(@PathVariable String title) {
//        log.info("Admin searching tasks with title containing: {}", title);
        return ResponseEntity.ok(agentService.searchTicketByTitle(title));
    }
    
    @GetMapping("/ticket/{id}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable Long id) {
//        log.info("Fetching task details for ID {}", id);
    	TicketDto ticket = agentService.getTicketById(id);
        if (ticket == null) {
//            log.warn("Task not found for ID {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
//        log.info("Task found: ID {}", task.getId());
        return ResponseEntity.ok(ticket);
    }
    
}
