package com.rasberryspotify.backend.controller;

import com.rasberryspotify.backend.security.CurrentUserProvider;
import com.rasberryspotify.core.dto.playlist.PlaylistCreateRequest;
import com.rasberryspotify.core.dto.playlist.PlaylistResponse;
import com.rasberryspotify.core.dto.playlist.PlaylistUpdateRequest;
import com.rasberryspotify.core.services.PlaylistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;
    private final CurrentUserProvider currentUser;

    @PostMapping
    public ResponseEntity<PlaylistResponse> create(
            @Valid @RequestBody PlaylistCreateRequest request) {
        PlaylistResponse response = playlistService.create(
                currentUser.getCurrentUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<PlaylistResponse>> getByOwner(@PathVariable UUID ownerId) {
        return ResponseEntity.ok(
                playlistService.getByOwner(ownerId, currentUser.getCurrentUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaylistResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                playlistService.getById(id, currentUser.getCurrentUserId()));
    }


    @PutMapping("/{id}")
    public ResponseEntity<PlaylistResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody PlaylistUpdateRequest request) {
        return ResponseEntity.ok(
                playlistService.update(id, currentUser.getCurrentUserId(), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        playlistService.delete(id, currentUser.getCurrentUserId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/tracks/{trackId}")
    public ResponseEntity<PlaylistResponse> addTrack(
            @PathVariable UUID id,
            @PathVariable UUID trackId) {
        return ResponseEntity.ok(
                playlistService.addTrack(id, trackId, currentUser.getCurrentUserId()));
    }

    @DeleteMapping("/{id}/tracks/{trackId}")
    public ResponseEntity<PlaylistResponse> removeTrack(
            @PathVariable UUID id,
            @PathVariable UUID trackId) {
        return ResponseEntity.ok(
                playlistService.removeTrack(id, trackId, currentUser.getCurrentUserId()));
    }
}