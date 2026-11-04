package com.rasberryspotify.backend.controller;

import com.rasberryspotify.backend.security.CurrentUserProvider;
import com.rasberryspotify.backend.storage.TrackStorageService;
import com.rasberryspotify.core.entities.Track;
import com.rasberryspotify.core.exception.AccessDeniedException;
import com.rasberryspotify.core.exception.NotFoundException;
import com.rasberryspotify.core.repositories.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/stream/tracks")
@RequiredArgsConstructor
public class TrackStreamingController {
    private final TrackStorageService storageService;
    private final CurrentUserProvider userProvider;
    private final TrackRepository trackRepository;

    @GetMapping("/upload-link")
    public ResponseEntity<Map<String, String>> getUploadLink(@RequestParam String fileName) {
        UUID userId = userProvider.getCurrentUserId();
        String bucket = userBucket(userId);
        String objectKey = "tracks/%s/%s".formatted(UUID.randomUUID(), sanitizeFileName(fileName));

        URL uploadUrl = storageService.presignPut(bucket, objectKey, Duration.ofMinutes(15));

        return ResponseEntity.ok(Map.of(
                "uploadUrl", uploadUrl.toString(),
                // в audio_url кладём "bucket/key" — как раньше
                "fileKey", bucket + "/" + objectKey
        ));
    }

    @GetMapping("/{trackId}/link")
    public ResponseEntity<Map<String, String>> getStreamLink(@PathVariable UUID trackId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new NotFoundException("Track not found"));

        UUID userId = userProvider.getCurrentUserId();
        if (!Boolean.TRUE.equals(track.getIsPublic())
                && (track.getUploadedBy() == null
                || !track.getUploadedBy().getId().equals(userId))) {
            throw new AccessDeniedException("This track is private");
        }

        var ref = parseAudioUrl(track.getAudioUrl());
        URL url = storageService.presignGet(ref.bucket(), ref.key(), Duration.ofMinutes(15));
        return ResponseEntity.ok(Map.of("url", url.toString()));
    }

    static String userBucket(UUID userId) {
        return "user-" + userId.toString().toLowerCase();
    }

    static String sanitizeFileName(String fileName) {
        if (fileName == null || fileName.isBlank()) return "audio";
        String base = fileName.replaceAll("[^A-Za-z0-9._-]", "_");
        return base.length() > 200 ? base.substring(base.length() - 200) : base;
    }

    record BucketObject(String bucket, String key) {}

    static BucketObject parseAudioUrl(String audioUrl) {
        if (audioUrl == null) {
            throw new IllegalStateException("audio_url is null");
        }
        int slash = audioUrl.indexOf('/');
        if (slash <= 0 || slash == audioUrl.length() - 1) {
            throw new IllegalStateException("Malformed audio_url: " + audioUrl);
        }
        return new BucketObject(audioUrl.substring(0, slash), audioUrl.substring(slash + 1));
    }
}