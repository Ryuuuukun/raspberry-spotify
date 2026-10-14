package com.rasberryspotify.backend.controllers;

import com.rasberryspotify.backend.storage.TrackStorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
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

    @GetMapping("/stream")
    public ResponseEntity<StreamingResponseBody> stream(@RequestParam("key") String key) throws IOException{
        InputStream in = storage.openStream(key);
        StreamingResponseBody body = out -> {
            try (in) {
                in.transferTo(out);
            }
        };
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(body);

    }
}
