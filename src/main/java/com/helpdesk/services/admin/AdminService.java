package com.helpdesk.services.admin;

import java.util.List;

import com.helpdesk.dto.TicketDto;
import com.helpdesk.dto.UserDto;
import com.helpdesk.entities.Department;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.TicketStatus;

public interface AdminService {

	List<UserDto> getUsers();
	
	List<TicketDto> getAllTickets();
	
//	List<TicketDto> getPendingTickets();

	TicketDto assignTicket(Long ticketId, Long agentId);
	
	List<TicketDto> searchTicketByTitle(String title);
	
	TicketDto getTicketById(Long id);
	
	List<TicketDto> getTicketsByPriority(Priority priority);
	
	List<TicketDto> getTicketsByTicketStatus(TicketStatus ticketStatus);
	
	List<TicketDto> getTicketsByDepartmentName(String name);

}
