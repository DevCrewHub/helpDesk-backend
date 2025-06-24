package com.helpdesk.services.admin;

import java.util.List;

import com.helpdesk.dto.TicketDto;
import com.helpdesk.dto.UserDto;

public interface AdminService {

	List<UserDto> getUsers();
	
	List<TicketDto> getAllTickets();
	
	List<TicketDto> getPendingTickets();

	TicketDto assignTicket(Long ticketId, Long agentId);

}
