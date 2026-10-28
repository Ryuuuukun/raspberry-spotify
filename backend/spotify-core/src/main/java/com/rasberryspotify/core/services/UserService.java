package com.rasberryspotify.core.services;

import com.rasberryspotify.core.dto.user.UserResponse;
import com.rasberryspotify.core.entities.User;
import com.rasberryspotify.core.exception.NotFoundException;
import com.rasberryspotify.core.map.UserMapper;
import com.rasberryspotify.core.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMap;

    @Transactional
    public void synchronizeUser(Jwt jwt) {
        var subject = jwt.getSubject();
        var username = jwt.getClaimAsString("preferred_username");
        if (subject == null || subject.isBlank()) {
            throw new IllegalStateException("The subject of the JWT token cannot be empty.");
        }

        userRepository.findByKeycloakId(UUID.fromString(subject))
                .ifPresentOrElse(
                        user -> {
                            if (!user.getUsername().equals(username)) {
                                user.setUsername(username);
                                userRepository.save(user);
                            }
                        },
                        () -> userRepository.save(User.builder()
                                .keycloakId(UUID.fromString(subject))
                                .username(username)
                                .build())
                );
    }

    @Transactional(readOnly = true)
    public UserResponse getByJwt(Jwt jwt) {
        var subject = jwt.getSubject();
        if (subject == null || subject.isBlank()) {
            throw new IllegalStateException("The subject of the JWT token cannot be empty.");
        }

        return getByKeycloakId(UUID.fromString(subject));
    }

    @Transactional(readOnly = true)
    public UserResponse getByKeycloakId(UUID id) {
        return userRepository.findByKeycloakId(id)
                .map(userMap::toResponse)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
    }

    @Transactional(readOnly = true)
    public UserResponse getById(UUID id) {
        return userRepository.findById(id)
                .map(userMap::toResponse)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
    }
}
