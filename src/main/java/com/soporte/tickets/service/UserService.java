package com.soporte.tickets.service;

import com.soporte.tickets.dto.request.RegisterRequest;
import com.soporte.tickets.dto.response.UserResponse;
import com.soporte.tickets.enums.Role;

import java.util.List;

public interface UserService {

    List<UserResponse> findAll();

    UserResponse findById(Long id);

    UserResponse update(Long id, RegisterRequest request);

    void delete(Long id);

    List<UserResponse> findByRole(Role role);

    void toggleEnabled(Long id);
}
