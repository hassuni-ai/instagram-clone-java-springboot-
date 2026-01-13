package com.example.instagram.controller;

import com.example.instagram.dto.request.CreateCommentRequest;
import com.example.instagram.dto.response.CommentResponse;
import com.example.instagram.dto.response.PageResponse;
import com.example.instagram.model.User;
import com.example.instagram.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable String postId,
            @Valid @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        CommentResponse response = commentService.addComment(postId, request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<CommentResponse>> getComments(
            @PathVariable String postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PageResponse<CommentResponse> response = commentService.getComments(postId, page, size);
        return ResponseEntity.ok(response);
    }
}
