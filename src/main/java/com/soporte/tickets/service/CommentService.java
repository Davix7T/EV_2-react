package com.soporte.tickets.service;

import com.soporte.tickets.dto.request.CommentRequest;
import com.soporte.tickets.dto.response.CommentResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface CommentService {

    CommentResponse addComment(Long ticketId, CommentRequest request, UserDetails currentUser);

    List<CommentResponse> findByTicket(Long ticketId);

    void delete(Long commentId, UserDetails currentUser);
}
