package com.helpdesk.controller.customer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.helpdesk.dto.TicketDto;
import com.helpdesk.services.customer.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class CustomerController {

	private final CustomerService customerService;

	@PostMapping("/ticket")
	public ResponseEntity<TicketDto> createTask(@RequestBody TicketDto ticketDto) {
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

}