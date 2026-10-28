package com.rasberryspotify.backend.controller;

import com.rasberryspotify.backend.security.CurrentUserProvider;
import com.rasberryspotify.core.dto.user.UserResponse;
import com.rasberryspotify.core.map.UserMapper;
import com.rasberryspotify.core.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final CurrentUserProvider userProvider;
    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe() {
        return ResponseEntity.ok(userMapper.toResponse(userProvider.getCurrentUser()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getById(id));
    }
}