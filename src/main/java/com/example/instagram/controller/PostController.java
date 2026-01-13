package com.example.instagram.controller;

import com.example.instagram.dto.request.CreatePostRequest;
import com.example.instagram.dto.response.LikeResponse;
import com.example.instagram.dto.response.PageResponse;
import com.example.instagram.dto.response.PostResponse;
import com.example.instagram.model.User;
import com.example.instagram.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @Valid @RequestBody CreatePostRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        PostResponse response = postService.createPost(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<PostResponse>> getFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User currentUser
    ) {
        PageResponse<PostResponse> response = postService.getFeed(page, size, currentUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(
            @PathVariable String postId,
            @AuthenticationPrincipal User currentUser
    ) {
        PostResponse response = postService.getPost(postId, currentUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable String postId,
            @AuthenticationPrincipal User currentUser
    ) {
        postService.deletePost(postId, currentUser);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<LikeResponse> likePost(
            @PathVariable String postId,
            @AuthenticationPrincipal User currentUser
    ) {
        LikeResponse response = postService.likePost(postId, currentUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<LikeResponse> unlikePost(
            @PathVariable String postId,
            @AuthenticationPrincipal User currentUser
    ) {
        LikeResponse response = postService.unlikePost(postId, currentUser);
        return ResponseEntity.ok(response);
    }
}
