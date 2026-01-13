package com.example.instagram.service;

import com.example.instagram.dto.request.CreateCommentRequest;
import com.example.instagram.dto.response.CommentResponse;
import com.example.instagram.dto.response.PageResponse;
import com.example.instagram.exception.ApiException;
import com.example.instagram.model.Comment;
import com.example.instagram.model.Post;
import com.example.instagram.model.User;
import com.example.instagram.repository.CommentRepository;
import com.example.instagram.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentResponse addComment(String postId, CreateCommentRequest request, User author) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", "Post not found"));

        Comment comment = Comment.builder()
                .post(post)
                .author(author)
                .text(request.getText())
                .build();

        comment = commentRepository.save(comment);
        return CommentResponse.from(comment, author);
    }

    public PageResponse<CommentResponse> getComments(String postId, int page, int size) {
        if (!postRepository.existsById(postId)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", "Post not found");
        }

        Page<Comment> commentsPage = commentRepository.findByPostIdOrderByCreatedAtDesc(
                postId, PageRequest.of(page, size));

        List<CommentResponse> comments = commentsPage.getContent().stream()
                .map(comment -> CommentResponse.from(comment, comment.getAuthor()))
                .toList();

        return PageResponse.from(commentsPage, comments);
    }
}
