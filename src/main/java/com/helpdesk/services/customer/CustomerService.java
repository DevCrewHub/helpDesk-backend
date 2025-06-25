package com.helpdesk.services.customer;

import java.util.List;

import com.helpdesk.dto.TicketDto;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.TicketStatus;

public interface CustomerService {

	TicketDto createTicket(TicketDto ticketDto);
	
	List<TicketDto> getAllTicketsCreated();
	
	void deleteTicket(Long id);

	TicketDto updateTicketStatus(Long ticketId, TicketStatus newStatus);
	
	TicketDto updateTicketPriority(Long ticketId, Priority newPriority);
	
	List<TicketDto> searchTicketByTitle(String title);
	
	TicketDto getTicketById(Long id);

}
