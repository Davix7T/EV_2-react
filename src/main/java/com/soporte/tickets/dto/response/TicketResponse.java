package com.soporte.tickets.dto.response;

import com.soporte.tickets.enums.Priority;
import com.soporte.tickets.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {

    private Long id;
    private String titulo;
    private String descripcion;
    private TicketStatus status;
    private Priority prioridad;

    private Long categoriaId;
    private String categoriaNombre;

    private Long creadoPorId;
    private String creadoPorUsername;

    private Long asignadoAId;
    private String asignadoAUsername;
    private String asignadoANombreCompleto;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime closedAt;
}
