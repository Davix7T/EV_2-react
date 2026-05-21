package com.soporte.tickets.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequest {

    @NotBlank(message = "El contenido del comentario es obligatorio")
    private String contenido;
}
