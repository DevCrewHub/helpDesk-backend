package com.helpdesk.services.customer;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

//import java.util.Optional;

import org.springframework.stereotype.Service;

import com.helpdesk.dto.TicketDto;
import com.helpdesk.entities.Ticket;
import com.helpdesk.entities.User;
import com.helpdesk.entities.Department;
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

}
