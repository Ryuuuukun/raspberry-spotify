package com.rasberryspotify.backend.security;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CurrentUserProvider {
    // TODO: заменить на извлечение из SecurityContext / JWT
    public UUID getCurrentUserId() {
        return UUID.fromString("00000000-0000-0000-0000-000000000001");
    }
}