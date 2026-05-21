package com.soporte.tickets.controller;

import com.soporte.tickets.dto.request.AssignTicketRequest;
import com.soporte.tickets.dto.request.TicketRequest;
import com.soporte.tickets.dto.request.UpdateStatusRequest;
import com.soporte.tickets.dto.response.ApiResponse;
import com.soporte.tickets.dto.response.TicketResponse;
import com.soporte.tickets.enums.Priority;
import com.soporte.tickets.enums.TicketStatus;
import com.soporte.tickets.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<ApiResponse<TicketResponse>> create(
            @Valid @RequestBody TicketRequest request,
            @AuthenticationPrincipal UserDetails currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Ticket creado exitosamente",
                        ticketService.create(request, currentUser)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TicketResponse>>> findAll(
            @AuthenticationPrincipal UserDetails currentUser) {
        return ResponseEntity.ok(ApiResponse.ok(ticketService.findAll(currentUser)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TicketResponse>> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails currentUser) {
        return ResponseEntity.ok(ApiResponse.ok(ticketService.findById(id, currentUser)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TicketResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody TicketRequest request,
            @AuthenticationPrincipal UserDetails currentUser) {
        return ResponseEntity.ok(ApiResponse.ok("Ticket actualizado",
                ticketService.update(id, request, currentUser)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        ticketService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Ticket eliminado", null));
    }

    @PatchMapping("/{id}/asignar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TicketResponse>> assign(
            @PathVariable Long id,
            @Valid @RequestBody AssignTicketRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Ticket asignado",
                ticketService.assign(id, request)));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO', 'USUARIO')")
    public ResponseEntity<ApiResponse<TicketResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request,
            @AuthenticationPrincipal UserDetails currentUser) {
        return ResponseEntity.ok(ApiResponse.ok("Estado actualizado",
                ticketService.updateStatus(id, request, currentUser)));
    }

    @GetMapping("/estado/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<ApiResponse<List<TicketResponse>>> findByStatus(
            @PathVariable TicketStatus status) {
        return ResponseEntity.ok(ApiResponse.ok(ticketService.findByStatus(status)));
    }

    @GetMapping("/prioridad/{prioridad}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<ApiResponse<List<TicketResponse>>> findByPriority(
            @PathVariable Priority prioridad) {
        return ResponseEntity.ok(ApiResponse.ok(ticketService.findByPriority(prioridad)));
    }

    @GetMapping("/sin-asignar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<TicketResponse>>> findSinAsignar() {
        return ResponseEntity.ok(ApiResponse.ok(ticketService.findSinAsignar()));
    }

    @GetMapping("/mis-tickets")
    public ResponseEntity<ApiResponse<List<TicketResponse>>> findMisTickets(
            @AuthenticationPrincipal UserDetails currentUser) {
        return ResponseEntity.ok(ApiResponse.ok(ticketService.findMisTickets(currentUser)));
    }
}
