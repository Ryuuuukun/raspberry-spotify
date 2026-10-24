package com.rasberryspotify.backend.controller;

import com.rasberryspotify.backend.security.CurrentUserProvider;
import com.rasberryspotify.core.dto.track.TrackCreateRequest;
import com.rasberryspotify.core.dto.track.TrackResponse;
import com.rasberryspotify.core.dto.track.TrackUpdateRequest;
import com.rasberryspotify.core.services.TrackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tracks")
@RequiredArgsConstructor
public class TrackController {
    private final TrackService trackService;
    private final CurrentUserProvider userProvider;

    @PostMapping
    public ResponseEntity<TrackResponse> create(
            @Valid @RequestBody TrackCreateRequest request) {
        TrackResponse response = trackService.create(
                userProvider.getCurrentUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<TrackResponse>> getMyTracks() {
        return ResponseEntity.ok(
                trackService.getMyTracks(userProvider.getCurrentUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrackResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                trackService.getById(id, userProvider.getCurrentUserId()));
    }


    @PutMapping("/{id}")
    public ResponseEntity<TrackResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody TrackUpdateRequest request) {
        return ResponseEntity.ok(
                trackService.update(id, userProvider.getCurrentUserId(), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        trackService.delete(id, userProvider.getCurrentUserId());
        return ResponseEntity.noContent().build();
    }
}