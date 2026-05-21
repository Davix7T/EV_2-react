package com.soporte.tickets.dto.response;

import com.soporte.tickets.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String nombre;
    private String apellido;
    private Role role;
    private boolean enabled;
    private LocalDateTime createdAt;
}
