package com.rasberryspotify.backend.controller;

import com.rasberryspotify.backend.security.CurrentUserProvider;
import com.rasberryspotify.core.dto.playlist.PlaylistCreateRequest;
import com.rasberryspotify.core.dto.playlist.PlaylistResponse;
import com.rasberryspotify.core.dto.playlist.PlaylistUpdateRequest;
import com.rasberryspotify.core.services.PlaylistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistController {
    private final CurrentUserProvider userProvider;
    private final PlaylistService playlistService;

    @PostMapping
    public ResponseEntity<PlaylistResponse> create(
            @Valid @RequestBody PlaylistCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(playlistService.create(userProvider.getCurrentUserId(), request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PlaylistResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody PlaylistUpdateRequest request) {

        return ResponseEntity.ok(playlistService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id) {

        playlistService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaylistResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(playlistService.getById(id));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<PlaylistResponse>> getByOwner() {
        return ResponseEntity.ok(playlistService.getByOwner(userProvider.getCurrentUserId()));
    }

    @GetMapping
    public ResponseEntity<Page<PlaylistResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(playlistService.getAll(pageable));
    }
}