package com.rasberryspotify.core.dto.user;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private UUID id;
    private String username;
    private String displayName;
    private String avatarUrl;
    private String bio;
    private Boolean isPrivate;
    private OffsetDateTime createdAt;
}
