package com.helpdesk.services.customer;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

//import java.util.Optional;

import org.springframework.stereotype.Service;

import com.helpdesk.dto.TicketDto;
import com.helpdesk.entities.Ticket;
import com.helpdesk.entities.User;
import com.helpdesk.entities.Department;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.TicketStatus;
import com.helpdesk.repositories.TicketRepository;
import com.helpdesk.repositories.DepartmentRepository;
//import com.helpdesk.repositories.UserRepository;
import com.helpdesk.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    // private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final JwtUtil jwtUtil;
    private final DepartmentRepository departmentRepository;

    @Override
    public TicketDto createTicket(TicketDto ticketDto) {
        // Get the currently logged-in user (customer)
        User loggedInUser = jwtUtil.getLoggedInUser();

        if (loggedInUser != null && ticketDto.getDepartmentName() != null) {
            Department department = departmentRepository.findByName(ticketDto.getDepartmentName()).orElse(null);
            if (department == null) {
                throw new RuntimeException("Department not found");
            }
            Ticket ticket = new Ticket();
            ticket.setTitle(ticketDto.getTitle());
            ticket.setDescription(ticketDto.getDescription());
            ticket.setPriority(ticketDto.getPriority());
            ticket.setDueDate(ticketDto.getDueDate());
            ticket.setTicketStatus(TicketStatus.PENDING); // Start with PENDING status
            ticket.setCustomer(loggedInUser); // Set the logged-in user as customer
            ticket.setDepartment(department);
            return ticketRepository.save(ticket).getTicketDto();
        }
        return null;
    }

    @Override
    public List<TicketDto> getAllTicketsCreated() {
        User loggedInUser = jwtUtil.getLoggedInUser();
        if (loggedInUser == null) {
            return List.of(); // or throw an exception
        }
        List<Ticket> tickets = ticketRepository.findByCustomer(loggedInUser);
        return tickets
        		.stream()
                .sorted(Comparator.comparing(Ticket::getDueDate).reversed())
                .map(Ticket::getTicketDto)
                .collect(Collectors.toList());
    }
    
    @Override
	public void deleteTicket(Long id) {
    	ticketRepository.deleteById(id);
	}

    @Override
    public TicketDto updateTicketStatus(Long ticketId, TicketStatus newStatus) {
        User customer = jwtUtil.getLoggedInUser();
        if (customer == null) {
            throw new RuntimeException("Customer not authenticated");
        }
        
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        if (optionalTicket.isEmpty()) {
            throw new RuntimeException("Ticket not found");
        }
        
        Ticket ticket = optionalTicket.get();
        
        // Check if the logged-in customer is the creator of this ticket
        if (!customer.equals(ticket.getCustomer())) {
            throw new RuntimeException("You can only update tickets created by you");
        }
        
        // Validate status transition based on new status
        if (newStatus == TicketStatus.CLOSED) {
            // Customer can close ticket anytime (except if already closed)
            if (ticket.getTicketStatus() == TicketStatus.CLOSED) {
                throw new RuntimeException("Ticket is already closed");
            }
        } else if (newStatus == TicketStatus.ASSIGNED) {
            // Customer can only reassign when ticket is RESOLVED
            if (ticket.getTicketStatus() != TicketStatus.RESOLVED) {
                throw new RuntimeException("You can only reassign ticket when it is RESOLVED");
            }
        } else {
            throw new RuntimeException("You can only change status to CLOSED or ASSIGNED");
        }
        
        // Update the status
        ticket.setTicketStatus(newStatus);
        return ticketRepository.save(ticket).getTicketDto();
    }
    
    @Override
    public TicketDto updateTicketPriority(Long ticketId, Priority newPriority) {
        User customer = jwtUtil.getLoggedInUser();
        if (customer == null) {
            throw new RuntimeException("Customer not authenticated");
        }
        
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        if (optionalTicket.isEmpty()) {
            throw new RuntimeException("Ticket not found");
        }
        
        Ticket ticket = optionalTicket.get();
        
        // Check if the logged-in customer is the creator of this ticket
        if (!customer.equals(ticket.getCustomer())) {
            throw new RuntimeException("You can only update tickets created by you");
        }

        // Prevent priority change if ticket is resolved or closed
        if (ticket.getTicketStatus() == TicketStatus.RESOLVED || ticket.getTicketStatus() == TicketStatus.CLOSED) {
            throw new RuntimeException("Cannot change priority of a resolved or closed ticket.");
        }
        
        ticket.setPriority(newPriority);
        return ticketRepository.save(ticket).getTicketDto();
    }
    
    @Override
    public List<TicketDto> searchTicketByTitle(String title) {
        User customer = jwtUtil.getLoggedInUser();
        if (customer != null) {
            return ticketRepository.findByCustomerAndTitleContaining(customer, title)
                .stream()
                .map(Ticket::getTicketDto)
                .collect(Collectors.toList());
        }
        return List.of();
    }

}
