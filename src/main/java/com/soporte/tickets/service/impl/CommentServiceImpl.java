package com.soporte.tickets.service.impl;

import com.soporte.tickets.dto.request.CommentRequest;
import com.soporte.tickets.dto.response.CommentResponse;
import com.soporte.tickets.entity.Comment;
import com.soporte.tickets.entity.Ticket;
import com.soporte.tickets.entity.User;
import com.soporte.tickets.enums.Role;
import com.soporte.tickets.exception.ResourceNotFoundException;
import com.soporte.tickets.repository.CommentRepository;
import com.soporte.tickets.repository.TicketRepository;
import com.soporte.tickets.repository.UserRepository;
import com.soporte.tickets.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CommentResponse addComment(Long ticketId, CommentRequest request, UserDetails currentUser) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", ticketId));
        User autor = getUser(currentUser.getUsername());

        Comment comment = Comment.builder()
                .ticket(ticket)
                .autor(autor)
                .contenido(request.getContenido())
                .build();

        return mapToResponse(commentRepository.save(comment));
    }

    @Override
    public List<CommentResponse> findByTicket(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new ResourceNotFoundException("Ticket", ticketId);
        }
        return commentRepository.findByTicketIdOrderByCreatedAtAsc(ticketId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long commentId, UserDetails currentUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario", commentId));
        User user = getUser(currentUser.getUsername());

        // Solo el autor o ADMIN puede eliminar
        if (user.getRole() != Role.ADMIN && !comment.getAutor().getId().equals(user.getId())) {
            throw new AccessDeniedException("No tiene permiso para eliminar este comentario");
        }
        commentRepository.deleteById(commentId);
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
    }

    private CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .ticketId(comment.getTicket().getId())
                .autorId(comment.getAutor().getId())
                .autorUsername(comment.getAutor().getUsername())
                .autorNombreCompleto(comment.getAutor().getNombre() + " " + comment.getAutor().getApellido())
                .contenido(comment.getContenido())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
