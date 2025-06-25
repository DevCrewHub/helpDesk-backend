package com.helpdesk.controller.customer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.helpdesk.dto.TicketDto;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.TicketStatus;
import com.helpdesk.services.customer.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class CustomerController {

	private final CustomerService customerService;

	@PostMapping("/ticket")
	public ResponseEntity<TicketDto> createTicket(@RequestBody TicketDto ticketDto) {
//        log.info("Admin creating new task: {}", taskDao.getTitle());
		TicketDto createTicketDto = customerService.createTicket(ticketDto);

		// If task creation fails, return BAD_REQUEST
		if (createTicketDto == null) {
//            log.warn("Failed to create task: {}", taskDao);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		// On success, return CREATED with the created task
//        log.info("Task created successfully with ID: {}", createTaskDao.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(createTicketDto);
	}
	
	@GetMapping("/ticketsCreated")
    public ResponseEntity<?> getAllTicketsCreated() {
//        log.info("Admin fetching all tasks.");
		return ResponseEntity.ok(customerService.getAllTicketsCreated());
    }
	
	@DeleteMapping("/ticket/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
//        log.info("Admin deleting task with ID: {}", id);
		customerService.deleteTicket(id);
        return ResponseEntity.ok(null);
    }
    
    @PutMapping("/tickets/{ticketId}/status")
    public ResponseEntity<?> updateTicketStatus(@PathVariable Long ticketId, @RequestParam String status) {
        try {
            TicketStatus newStatus = TicketStatus.valueOf(status.toUpperCase());
            TicketDto updatedTicket = customerService.updateTicketStatus(ticketId, newStatus);
            return ResponseEntity.ok(updatedTicket);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status: " + status);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/tickets/{ticketId}/priority")
    public ResponseEntity<?> updateTicketPriority(@PathVariable Long ticketId, @RequestParam String priority) {
        try {
            Priority newPriority = Priority.valueOf(priority.toUpperCase());
            TicketDto updatedTicket = customerService.updateTicketPriority(ticketId, newPriority);
            return ResponseEntity.ok(updatedTicket);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid priority: " + priority);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/tickets/search/{title}")
    public ResponseEntity<?> searchTicketsByTitle(@PathVariable String title) {
        return ResponseEntity.ok(customerService.searchTicketByTitle(title));
    }

}