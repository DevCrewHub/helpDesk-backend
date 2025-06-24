package com.helpdesk.services.customer;

import java.util.List;

import com.helpdesk.dto.TicketDto;

public interface CustomerService {

	TicketDto createTicket(TicketDto ticketDto);
	
	List<TicketDto> getAllTicketsCreated();

}
