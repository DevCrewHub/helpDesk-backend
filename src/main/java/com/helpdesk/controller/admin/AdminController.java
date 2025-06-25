package com.helpdesk.controller.admin;

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
import com.helpdesk.services.admin.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

	private final AdminService adminService;

	@GetMapping("/users")
	public ResponseEntity<?> getUsers() {
//        log.info("Admin requested user list.");
		return ResponseEntity.ok(adminService.getUsers());
	}
	
	@GetMapping("/tickets")
    public ResponseEntity<?> getAllTickets() {
//        log.info("Admin fetching all tasks.");
        return ResponseEntity.ok(adminService.getAllTickets());
    }
	
	@GetMapping("/tickets/pending")
    public ResponseEntity<?> getPendingTickets() {
        return ResponseEntity.ok(adminService.getPendingTickets());
    }

    @PutMapping("/tickets/{ticketId}/assign")
    public ResponseEntity<?> assignTicket(@PathVariable Long ticketId, @RequestParam Long agentId) {
        try {
            TicketDto updatedTicket = adminService.assignTicket(ticketId, agentId);
            return ResponseEntity.ok(updatedTicket);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/tickets/search/{title}")
    public ResponseEntity<List<TicketDto>> searchTask(@PathVariable String title) {
//        log.info("Admin searching tasks with title containing: {}", title);
        return ResponseEntity.ok(adminService.searchTicketByTitle(title));
    }
    
    @GetMapping("/ticket/{id}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable Long id) {
//        log.info("Fetching task details for ID {}", id);
    	TicketDto ticket = adminService.getTicketById(id);
        if (ticket == null) {
//            log.warn("Task not found for ID {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
//        log.info("Task found: ID {}", task.getId());
        return ResponseEntity.ok(ticket);
    }

}
