package com.soporte.tickets.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignTicketRequest {

    @NotNull(message = "El ID del técnico es obligatorio")
    private Long tecnicoId;
}
