package com.helpdesk.services.agent;

import java.util.List;
import com.helpdesk.dto.TicketDto;

public interface AgentService {
    List<TicketDto> getAssignedTickets();
}