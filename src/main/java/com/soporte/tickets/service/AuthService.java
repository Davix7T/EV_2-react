package com.soporte.tickets.service;

import com.soporte.tickets.dto.request.LoginRequest;
import com.soporte.tickets.dto.request.RegisterRequest;
import com.soporte.tickets.dto.response.JwtResponse;
import com.soporte.tickets.dto.response.UserResponse;

public interface AuthService {

    JwtResponse login(LoginRequest request);

    UserResponse register(RegisterRequest request);
}
