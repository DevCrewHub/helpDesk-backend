package com.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.helpdesk.entities.Ticket;
import com.helpdesk.entities.User;
import com.helpdesk.enums.TicketStatus;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByCustomer(User customer);
    
    List<Ticket> findByTicketStatus(TicketStatus ticketStatus);

    List<Ticket> findByAssignedAgent(User assignedAgent);
    
    List<Ticket> findAllByTitleContaining(String title);

    List<Ticket> findByAssignedAgentAndTitleContaining(User assignedAgent, String title);
    
    List<Ticket> findByCustomerAndTitleContaining(User customer, String title);

}
