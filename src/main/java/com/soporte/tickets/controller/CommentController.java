package com.soporte.tickets.controller;

import com.soporte.tickets.dto.request.CommentRequest;
import com.soporte.tickets.dto.response.ApiResponse;
import com.soporte.tickets.dto.response.CommentResponse;
import com.soporte.tickets.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets/{ticketId}/comentarios")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponse>>> findAll(@PathVariable Long ticketId) {
        return ResponseEntity.ok(ApiResponse.ok(commentService.findByTicket(ticketId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> addComment(
            @PathVariable Long ticketId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserDetails currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Comentario agregado",
                        commentService.addComment(ticketId, request, currentUser)));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long ticketId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails currentUser) {
        commentService.delete(commentId, currentUser);
        return ResponseEntity.ok(ApiResponse.ok("Comentario eliminado", null));
    }
}
