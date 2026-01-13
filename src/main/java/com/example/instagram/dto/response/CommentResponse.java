package com.example.instagram.dto.response;

import com.example.instagram.model.Comment;
import com.example.instagram.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private String id;
    private String text;
    private AuthorSummary author;
    private Instant createdAt;

    public static CommentResponse from(Comment comment, User author) {
        return CommentResponse.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(AuthorSummary.from(author))
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
