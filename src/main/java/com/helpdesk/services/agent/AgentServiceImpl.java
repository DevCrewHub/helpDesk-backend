package com.helpdesk.services.agent;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.helpdesk.dto.TicketDto;
import com.helpdesk.entities.Ticket;
import com.helpdesk.entities.User;
import com.helpdesk.enums.TicketStatus;
import com.helpdesk.repositories.TicketRepository;
import com.helpdesk.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {

    private final TicketRepository ticketRepository;
    private final JwtUtil jwtUtil;

    @Override
    public List<TicketDto> getAssignedTickets() {
        User agent = jwtUtil.getLoggedInUser();
        if (agent != null) {
            return ticketRepository.findByAssignedAgent(agent)
                    .stream()
                    .sorted(Comparator.comparing(Ticket::getDueDate).reversed())
                    .map(Ticket::getTicketDto)
                    .collect(Collectors.toList());
        }
        return List.of();
    }
    
    @Override
    public TicketDto updateTicketStatus(Long ticketId, TicketStatus newStatus) {
        User agent = jwtUtil.getLoggedInUser();
        if (agent == null) {
            throw new RuntimeException("Agent not authenticated");
        }
        
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        if (optionalTicket.isEmpty()) {
            throw new RuntimeException("Ticket not found");
        }
        
        Ticket ticket = optionalTicket.get();
        
        // Check if the logged-in agent is assigned to this ticket
        if (!agent.equals(ticket.getAssignedAgent())) {
            throw new RuntimeException("You can only update tickets assigned to you");
        }
        
        // Validate status transition
        if (!isValidStatusTransition(ticket.getTicketStatus(), newStatus)) {
            throw new RuntimeException("Invalid status transition from " + ticket.getTicketStatus() + " to " + newStatus);
        }
        
        // Update the status
        ticket.setTicketStatus(newStatus);
        return ticketRepository.save(ticket).getTicketDto();
    }
    
    private boolean isValidStatusTransition(TicketStatus currentStatus, TicketStatus newStatus) {
        // Allow transitions: ASSIGNED -> INPROGRESS -> RESOLVED
        if (currentStatus == TicketStatus.ASSIGNED && newStatus == TicketStatus.INPROGRESS) {
            return true;
        }
        if (currentStatus == TicketStatus.INPROGRESS && newStatus == TicketStatus.RESOLVED) {
            return true;
        }
        if (currentStatus == TicketStatus.ASSIGNED && newStatus == TicketStatus.RESOLVED) {
            return true; // Allow direct transition from ASSIGNED to RESOLVED
        }
        return false;
    }
    
    @Override
    public List<TicketDto> searchTicketByTitle(String title) {
        User agent = jwtUtil.getLoggedInUser();
        if (agent != null) {
            return ticketRepository.findByAssignedAgentAndTitleContaining(agent, title)
                    .stream()
                    .map(Ticket::getTicketDto)
                    .collect(Collectors.toList());
        }
        return List.of();
    }
    
    @Override
	public TicketDto getTicketById(Long id) {
    	User agent = jwtUtil.getLoggedInUser();
		return Optional.ofNullable(ticketRepository.findTicketByAssignedAgentAndId(agent, id))
		    .map(Ticket::getTicketDto)
		    .orElse(null);
	}
    
}
