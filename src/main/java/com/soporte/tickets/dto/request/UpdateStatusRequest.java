package com.soporte.tickets.dto.request;

import com.soporte.tickets.enums.TicketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStatusRequest {

    @NotNull(message = "El estado es obligatorio")
    private TicketStatus status;
}
