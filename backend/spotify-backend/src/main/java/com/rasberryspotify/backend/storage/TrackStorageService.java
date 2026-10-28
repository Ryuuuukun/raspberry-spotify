package com.rasberryspotify.backend.storage;

import io.awspring.cloud.s3.S3Template;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@Service
public class TrackStorageService {

    private final S3Template s3Template;
    private final com.rasberryspotify.backend.storage.StorageProperties properties;

    public TrackStorageService(S3Template s3Template, com.rasberryspotify.backend.storage.StorageProperties properties) {
        this.s3Template = s3Template;
        this.properties = properties;
    }


    public String upload(MultipartFile file, UUID trackId) throws IOException {
        String key = "tracks/" + trackId + "/original" + extension(file.getOriginalFilename());
        try (InputStream in = file.getInputStream()) {
            s3Template.upload(properties.getBucket(), key, in);
        }
        return key;
    }

    public URL generateUploadUrl(String key, Duration ttl) {
        return s3Template.createSignedPutURL(properties.getBucket(), key, ttl);
    }

    public URL generatePresignedUrl(String key, Duration ttl) {
        return s3Template.createSignedGetURL(properties.getBucket(), key, ttl);
    }

    public void delete(String key) {
        s3Template.deleteObject(properties.getBucket(), key);
    }

    private String extension(String filename) {
        if (filename == null) return "";
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot) : "";
    }
}


