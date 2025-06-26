package com.helpdesk.services.agent;

import java.util.List;
import com.helpdesk.dto.TicketDto;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.TicketStatus;

public interface AgentService {
    List<TicketDto> getAssignedTickets();
    
    TicketDto updateTicketStatus(Long ticketId, TicketStatus newStatus);
    
    List<TicketDto> searchTicketByTitle(String title);
    
    TicketDto getTicketById(Long id);
    
    List<TicketDto> getTicketsByPriority(Priority priority);
    
    List<TicketDto> getTicketsByTicketStatus(TicketStatus ticketStatus);
    
}
