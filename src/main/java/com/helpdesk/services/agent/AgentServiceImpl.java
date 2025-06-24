package com.helpdesk.services.agent;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.helpdesk.dto.TicketDto;
import com.helpdesk.entities.Ticket;
import com.helpdesk.entities.User;
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
                    .map(Ticket::getTicketDto)
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}
