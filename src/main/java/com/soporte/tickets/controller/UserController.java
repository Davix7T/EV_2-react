package com.soporte.tickets.controller;

import com.soporte.tickets.dto.request.RegisterRequest;
import com.soporte.tickets.dto.response.ApiResponse;
import com.soporte.tickets.dto.response.UserResponse;
import com.soporte.tickets.enums.Role;
import com.soporte.tickets.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.ok(userService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(userService.findById(id)));
    }

    @GetMapping("/rol/{role}")
    public ResponseEntity<ApiResponse<List<UserResponse>>> findByRole(@PathVariable Role role) {
        return ResponseEntity.ok(ApiResponse.ok(userService.findByRole(role)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Usuario actualizado", userService.update(id, request)));
    }

    @PatchMapping("/{id}/toggle-estado")
    public ResponseEntity<ApiResponse<Void>> toggleEnabled(@PathVariable Long id) {
        userService.toggleEnabled(id);
        return ResponseEntity.ok(ApiResponse.ok("Estado del usuario actualizado", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Usuario eliminado", null));
    }
}
