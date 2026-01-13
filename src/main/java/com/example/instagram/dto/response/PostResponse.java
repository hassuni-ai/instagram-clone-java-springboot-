package com.example.instagram.dto.response;

import com.example.instagram.model.Post;
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
public class PostResponse {

    private String id;
    private String imageUrl;
    private String caption;
    private AuthorSummary author;
    private long likesCount;
    private long commentsCount;
    private boolean isLiked;
    private Instant createdAt;

    public static PostResponse from(Post post, User author, long likesCount, long commentsCount, boolean isLiked) {
        return PostResponse.builder()
                .id(post.getId())
                .imageUrl(post.getImageUrl())
                .caption(post.getCaption())
                .author(AuthorSummary.from(author))
                .likesCount(likesCount)
                .commentsCount(commentsCount)
                .isLiked(isLiked)
                .createdAt(post.getCreatedAt())
                .build();
    }
}
