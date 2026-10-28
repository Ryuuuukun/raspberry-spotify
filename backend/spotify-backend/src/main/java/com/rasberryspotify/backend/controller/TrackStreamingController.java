package com.rasberryspotify.backend.controller;

import com.rasberryspotify.backend.security.CurrentUserProvider;
import com.rasberryspotify.backend.storage.TrackStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @GetMapping("/upload-link")
    public ResponseEntity<Map<String, String>> getUploadLink(@RequestParam String fileName) {

        UUID currentUserId = userProvider.getCurrentUserId();
        String fileKey = "users/" + currentUserId + "/tracks/" + UUID.randomUUID() + "/" + fileName;
        URL uploadUrl = storageService.generateUploadUrl(fileKey, Duration.ofMinutes(15));

        return ResponseEntity.ok(Map.of(
                "uploadUrl", uploadUrl.toString(),
                "fileKey", fileKey
        ));

    }

    @GetMapping("/link")
    public ResponseEntity<Map<String, String>> getStreamLink(@RequestParam String fileKey) {
//        String key = "tracks/" + id + "/original.mp3";
        URL presignedUrl = this.storageService.generatePresignedUrl(fileKey, Duration.ofMinutes(15));

        return ResponseEntity.ok(Map.of("url", presignedUrl.toString()));
    }
}
