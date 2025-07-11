package com.helpdesk.services.admin;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.helpdesk.dto.TicketDto;
import com.helpdesk.dto.UserDto;
import com.helpdesk.entities.Department;
import com.helpdesk.entities.Ticket;
import com.helpdesk.entities.User;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.TicketStatus;
import com.helpdesk.enums.UserRole;
import com.helpdesk.repositories.TicketRepository;
import com.helpdesk.repositories.UserRepository;
import com.helpdesk.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;
	private final TicketRepository ticketRepository;

	@Override
	public List<UserDto> getUsers() {
		return userRepository.findAll().stream().filter(user -> {
			UserRole role = user.getUserRole();
			return role == UserRole.CUSTOMER || role == UserRole.AGENT;
		}).map(User::getUserDto).collect(Collectors.toList());
	}
	
	@Override
	public List<TicketDto> getAllTickets() {
		return ticketRepository.findAll()
				.stream()
				.sorted(Comparator.comparing(Ticket::getDueDate).reversed())
				.map(Ticket::getTicketDto)
				.collect(Collectors.toList());
	}
	
//	@Override
//    public List<TicketDto> getPendingTickets() {
//        return ticketRepository.findByTicketStatus(TicketStatus.PENDING)
//                .stream().map(Ticket::getTicketDto).collect(Collectors.toList());
//    }

    @Override
    public TicketDto assignTicket(Long ticketId, Long agentId) {
        User admin = jwtUtil.getLoggedInUser();
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        Optional<User> optionalAgent = userRepository.findById(agentId);

        if (optionalTicket.isPresent() && optionalAgent.isPresent() && optionalAgent.get().getUserRole() == UserRole.AGENT) {
            Ticket ticket = optionalTicket.get();
            User agent = optionalAgent.get();

            if (ticket.getDepartment().equals(agent.getDepartment())) {
                ticket.setAssignedAgent(agent);
                ticket.setTicketStatus(TicketStatus.ASSIGNED);
                return ticketRepository.save(ticket).getTicketDto();
            } else {
                throw new RuntimeException("Agent's department does not match the ticket's department.");
            }
        }
        throw new RuntimeException("Ticket or Agent not found or invalid role.");
    }
    
    @Override
	public List<TicketDto> searchTicketByTitle(String title) {
		return ticketRepository.findAllByTitleContaining(title)
				.stream()
				.sorted(Comparator.comparing(Ticket::getDueDate).reversed())
				.map(Ticket::getTicketDto)
				.collect(Collectors.toList());
	}
    
    @Override
	public TicketDto getTicketById(Long id) {
		Optional<Ticket> optionalTicket = ticketRepository.findById(id);
		return optionalTicket.map(Ticket::getTicketDto).orElse(null);
	}
    
    @Override
	public List<TicketDto> getTicketsByPriority(Priority priority) {
		return ticketRepository.findByPriority(priority)
				.stream()
				.sorted(Comparator.comparing(Ticket::getDueDate).reversed())
				.map(Ticket::getTicketDto)
				.collect(Collectors.toList());
	}
    
    @Override
	public List<TicketDto> getTicketsByTicketStatus(TicketStatus ticketStatus) {
		return ticketRepository.findByTicketStatus(ticketStatus)
				.stream()
				.sorted(Comparator.comparing(Ticket::getDueDate).reversed())
				.map(Ticket::getTicketDto)
				.collect(Collectors.toList());
	}
    
    @Override
	public List<TicketDto> getTicketsByDepartmentName(String name) {
		return ticketRepository.findByDepartmentName(name)
				.stream()
				.sorted(Comparator.comparing(Ticket::getDueDate).reversed())
				.map(Ticket::getTicketDto)
				.collect(Collectors.toList());
	}

}