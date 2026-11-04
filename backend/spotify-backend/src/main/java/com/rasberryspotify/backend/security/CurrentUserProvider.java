package com.rasberryspotify.backend.security;

import com.rasberryspotify.core.entities.User;
import com.rasberryspotify.core.exception.NotFoundException;
import com.rasberryspotify.core.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CurrentUserProvider {
    private final UserRepository userRepository;

    public UUID getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt
                && jwt.getSubject() != null && !jwt.getSubject().isBlank()) {

            return userRepository.findByKeycloakId(UUID.fromString(jwt.getSubject()))
                    .orElseThrow(() -> new NotFoundException("User was not found."));
        }

        throw new IllegalStateException("The user is not authenticated or the token is missing.");
    }
}