package com.soporte.tickets.service.impl;

import com.soporte.tickets.dto.request.RegisterRequest;
import com.soporte.tickets.dto.response.UserResponse;
import com.soporte.tickets.entity.User;
import com.soporte.tickets.enums.Role;
import com.soporte.tickets.exception.BadRequestException;
import com.soporte.tickets.exception.ResourceNotFoundException;
import com.soporte.tickets.repository.UserRepository;
import com.soporte.tickets.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public UserResponse findById(Long id) {
        return mapToResponse(getUser(id));
    }

    @Override
    @Transactional
    public UserResponse update(Long id, RegisterRequest request) {
        User user = getUser(id);

        if (!user.getUsername().equals(request.getUsername())
                && userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("El username '" + request.getUsername() + "' ya está en uso");
        }
        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email '" + request.getEmail() + "' ya está registrado");
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setNombre(request.getNombre());
        user.setApellido(request.getApellido());
        user.setRole(request.getRole());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return mapToResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<UserResponse> findByRole(Role role) {
        return userRepository.findByRole(role).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public void toggleEnabled(Long id) {
        User user = getUser(id);
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .nombre(user.getNombre())
                .apellido(user.getApellido())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
