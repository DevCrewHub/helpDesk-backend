package com.helpdesk.dto;

import java.util.Date;

import com.helpdesk.enums.Priority;
import com.helpdesk.enums.TicketStatus;

import lombok.Data;

@Data
public class TicketDto {

	private Long id;

	private String title;

	private String description;

	private Date dueDate;

	private Priority priority;

	private TicketStatus ticketStatus;

	// Auto-set from logged-in user, not required in request
	private Long customerId;

	// Auto-set from logged-in user, not required in request
	private String customerName;

	private Long agentId;

	private String agentName;

	private Long departmentId;

	private String departmentName;

}