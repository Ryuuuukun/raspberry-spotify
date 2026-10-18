package com.rasberryspotify.backend.controllers;

import com.rasberryspotify.backend.storage.TrackStorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/tracks")
public class TrackController {
    private final TrackStorageService storage;

    public TrackController(TrackStorageService storage) {
        this.storage = storage;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        UUID trackId = UUID.randomUUID();
        String key = storage.upload(file, trackId);
        return ResponseEntity.ok(key);
    }

    @GetMapping("/{id}/link")
    public ResponseEntity<Map<String, String>> getStreamLink(@PathVariable UUID id) {
        // Пока БД нет, формируем ключ "на лету" (как при загрузке)
        // В будущем: достаем реальный ключ из БД по id трека
        String key = "tracks/" + id + "/original.mp3";

        URL presignedUrl = storage.generatePresignedUrl(key, Duration.ofMinutes(15));

        return ResponseEntity.ok(Map.of("url", presignedUrl.toString()));
    }
}

