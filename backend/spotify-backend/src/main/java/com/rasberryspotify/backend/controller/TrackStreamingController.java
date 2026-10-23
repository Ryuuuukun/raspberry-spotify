package com.rasberryspotify.backend.controller;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String key = this.storageService.upload(file, UUID.randomUUID());

        return ResponseEntity.ok(key);
    }

    @GetMapping("/{id}/link")
    public ResponseEntity<Map<String, String>> getStreamLink(@PathVariable UUID id) {
        String key = "tracks/" + id + "/original.mp3";
        URL presignedUrl = this.storageService.generatePresignedUrl(key, Duration.ofMinutes(15));

        return ResponseEntity.ok(Map.of("url", presignedUrl.toString()));
    }
}
