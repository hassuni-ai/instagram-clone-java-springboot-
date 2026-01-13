package com.example.instagram.dto.response;

import com.example.instagram.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorSummary {

    private String id;
    private String username;
    private String avatarUrl;

    public static AuthorSummary from(User user) {
        return AuthorSummary.builder()
                .id(user.getId())
                .username(user.getUsername())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}
