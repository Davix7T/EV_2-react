package com.soporte.tickets.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soporte.tickets.dto.request.AssignTicketRequest;
import com.soporte.tickets.dto.request.TicketRequest;
import com.soporte.tickets.dto.request.UpdateStatusRequest;
import com.soporte.tickets.dto.response.TicketResponse;
import com.soporte.tickets.entity.Category;
import com.soporte.tickets.entity.Ticket;
import com.soporte.tickets.entity.User;
import com.soporte.tickets.enums.Priority;
import com.soporte.tickets.enums.Role;
import com.soporte.tickets.enums.TicketStatus;
import com.soporte.tickets.exception.BadRequestException;
import com.soporte.tickets.exception.ResourceNotFoundException;
import com.soporte.tickets.repository.CategoryRepository;
import com.soporte.tickets.repository.TicketRepository;
import com.soporte.tickets.repository.UserRepository;
import com.soporte.tickets.service.TicketService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public TicketResponse create(TicketRequest request, UserDetails currentUser) {
        User creator = getUser(currentUser.getUsername());

        Category category = null;
        if (request.getCategoriaId() != null) {
            category = categoryRepository.findById(request.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría", request.getCategoriaId()));
        }

        Ticket ticket = Ticket.builder()
                .titulo(request.getTitulo())
                .descripcion(request.getDescripcion())
                .prioridad(request.getPrioridad())
                .categoria(category)
                .creadoPor(creator)
                .build();

        return mapToResponse(ticketRepository.save(ticket));
    }

    @Override
    @Transactional
    public List<TicketResponse> findAll(UserDetails currentUser) {
        log.info("findAll called with currentUser: {}", currentUser);
        
        if (currentUser == null) {
            log.error("currentUser is null!");
            throw new BadRequestException("Usuario no autenticado");
        }
        
        String username = currentUser.getUsername();
        log.info("Username from currentUser: {}", username);
        
        User user = getUser(username);
        log.info("Found user: {}", user.getUsername());

        // ADMIN y TECNICO ven todos; USUARIO solo ve los suyos
        if (user.getRole() == Role.USUARIO) {
            log.info("User {} is USUARIO, returning only their tickets", username);
            return ticketRepository.findByCreadoPor(user).stream()
                    .map(this::mapToResponse).toList();
        }
        log.info("User {} is {} or ADMIN, returning all tickets", username, user.getRole());
        return ticketRepository.findAll().stream()
                .map(this::mapToResponse).toList();
    }

    @Override
    @Transactional
    public TicketResponse findById(Long id, UserDetails currentUser) {
        Ticket ticket = getTicket(id);
        User user = getUser(currentUser.getUsername());

        if (user.getRole() == Role.USUARIO && !ticket.getCreadoPor().getId().equals(user.getId())) {
            throw new AccessDeniedException("No tiene acceso a este ticket");
        }
        return mapToResponse(ticket);
    }

    @Override
    @Transactional
    public TicketResponse update(Long id, TicketRequest request, UserDetails currentUser) {
        Ticket ticket = getTicket(id);
        User user = getUser(currentUser.getUsername());

        // Solo el creador o ADMIN puede editar
        if (user.getRole() == Role.USUARIO && !ticket.getCreadoPor().getId().equals(user.getId())) {
            throw new AccessDeniedException("Solo puede editar sus propios tickets");
        }
        // No se puede editar un ticket cerrado o resuelto (solo ADMIN puede)
        if (user.getRole() != Role.ADMIN
                && (ticket.getStatus() == TicketStatus.CERRADO || ticket.getStatus() == TicketStatus.RESUELTO)) {
            throw new BadRequestException("No se puede editar un ticket cerrado o resuelto");
        }

        ticket.setTitulo(request.getTitulo());
        ticket.setDescripcion(request.getDescripcion());
        ticket.setPrioridad(request.getPrioridad());

        if (request.getCategoriaId() != null) {
            Category category = categoryRepository.findById(request.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría", request.getCategoriaId()));
            ticket.setCategoria(category);
        }

        return mapToResponse(ticketRepository.save(ticket));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ticket", id);
        }
        ticketRepository.deleteById(id);
    }

    @Override
    @Transactional
    public TicketResponse assign(Long id, AssignTicketRequest request) {
        Ticket ticket = getTicket(id);
        User tecnico = userRepository.findById(request.getTecnicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Técnico", request.getTecnicoId()));

        if (tecnico.getRole() != Role.TECNICO && tecnico.getRole() != Role.ADMIN) {
            throw new BadRequestException("El usuario seleccionado no tiene rol de técnico");
        }

        ticket.setAsignadoA(tecnico);
        if (ticket.getStatus() == TicketStatus.ABIERTO) {
            ticket.setStatus(TicketStatus.EN_PROCESO);
        }
        return mapToResponse(ticketRepository.save(ticket));
    }

    @Override
    @Transactional
    public TicketResponse updateStatus(Long id, UpdateStatusRequest request, UserDetails currentUser) {
        Ticket ticket = getTicket(id);
        User user = getUser(currentUser.getUsername());

        // USUARIO solo puede cerrar sus propios tickets
        if (user.getRole() == Role.USUARIO) {
            if (!ticket.getCreadoPor().getId().equals(user.getId())) {
                throw new AccessDeniedException("No tiene permiso para cambiar el estado de este ticket");
            }
            if (request.getStatus() != TicketStatus.CERRADO) {
                throw new BadRequestException("Los usuarios solo pueden cerrar tickets");
            }
        }

        ticket.setStatus(request.getStatus());
        if (request.getStatus() == TicketStatus.CERRADO || request.getStatus() == TicketStatus.RESUELTO) {
            ticket.setClosedAt(LocalDateTime.now());
        }
        return mapToResponse(ticketRepository.save(ticket));
    }

    @Override
    @Transactional
    public List<TicketResponse> findByStatus(TicketStatus status) {
        return ticketRepository.findByStatus(status).stream()
                .map(this::mapToResponse).toList();
    }

    @Override
    @Transactional
    public List<TicketResponse> findByPriority(Priority prioridad) {
        return ticketRepository.findByPrioridad(prioridad).stream()
                .map(this::mapToResponse).toList();
    }

    @Override
    @Transactional
    public List<TicketResponse> findSinAsignar() {
        return ticketRepository.findTicketsSinAsignar().stream()
                .map(this::mapToResponse).toList();
    }

    @Override
    @Transactional
    public List<TicketResponse> findMisTickets(UserDetails currentUser) {
        User user = getUser(currentUser.getUsername());
        if (user.getRole() == Role.TECNICO) {
            return ticketRepository.findByAsignadoA(user).stream()
                    .map(this::mapToResponse).toList();
        }
        return ticketRepository.findByCreadoPor(user).stream()
                .map(this::mapToResponse).toList();
    }

    private Ticket getTicket(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", id));
    }

    private User getUser(String username) {
        log.debug("Searching for user: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado: {}", username);
                    return new ResourceNotFoundException("Usuario no encontrado: " + username);
                });
    }

    private TicketResponse mapToResponse(Ticket ticket) {
        User creadoPor = ticket.getCreadoPor();
        User asignadoA = ticket.getAsignadoA();
        Category categoria = ticket.getCategoria();
        
        return TicketResponse.builder()
                .id(ticket.getId())
                .titulo(ticket.getTitulo())
                .descripcion(ticket.getDescripcion())
                .status(ticket.getStatus())
                .prioridad(ticket.getPrioridad())
                .categoriaId(categoria != null ? categoria.getId() : null)
                .categoriaNombre(categoria != null ? categoria.getNombre() : null)
                .creadoPorId(creadoPor != null ? creadoPor.getId() : null)
                .creadoPorUsername(creadoPor != null ? creadoPor.getUsername() : null)
                .asignadoAId(asignadoA != null ? asignadoA.getId() : null)
                .asignadoAUsername(asignadoA != null ? asignadoA.getUsername() : null)
                .asignadoANombreCompleto(asignadoA != null
                        ? asignadoA.getNombre() + " " + asignadoA.getApellido()
                        : null)
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .closedAt(ticket.getClosedAt())
                .build();
    }
}
