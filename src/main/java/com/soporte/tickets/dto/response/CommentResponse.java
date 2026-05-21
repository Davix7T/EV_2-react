package com.soporte.tickets.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private Long id;
    private Long ticketId;
    private Long autorId;
    private String autorUsername;
    private String autorNombreCompleto;
    private String contenido;
    private LocalDateTime createdAt;
}
