package com.soporte.tickets.service;

import com.soporte.tickets.dto.request.AssignTicketRequest;
import com.soporte.tickets.dto.request.TicketRequest;
import com.soporte.tickets.dto.request.UpdateStatusRequest;
import com.soporte.tickets.dto.response.TicketResponse;
import com.soporte.tickets.enums.Priority;
import com.soporte.tickets.enums.TicketStatus;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface TicketService {

    TicketResponse create(TicketRequest request, UserDetails currentUser);

    List<TicketResponse> findAll(UserDetails currentUser);

    TicketResponse findById(Long id, UserDetails currentUser);

    TicketResponse update(Long id, TicketRequest request, UserDetails currentUser);

    void delete(Long id);

    TicketResponse assign(Long id, AssignTicketRequest request);

    TicketResponse updateStatus(Long id, UpdateStatusRequest request, UserDetails currentUser);

    List<TicketResponse> findByStatus(TicketStatus status);

    List<TicketResponse> findByPriority(Priority prioridad);

    List<TicketResponse> findSinAsignar();

    List<TicketResponse> findMisTickets(UserDetails currentUser);
}
