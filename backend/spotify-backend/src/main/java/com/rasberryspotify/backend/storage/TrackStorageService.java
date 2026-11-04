package com.rasberryspotify.backend.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrackStorageService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

     private final Set<String> knownBuckets = ConcurrentHashMap.newKeySet();

    public void ensureBucketExists(String bucket) {
        if (knownBuckets.contains(bucket)) return;

        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
        } catch (S3Exception e) {
            if (e.statusCode() != 404) {
                // Не 404 — реальная ошибка (403, 5xx). Не глотаем.
                throw e;
            }
            try {
                s3Client.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
                log.info("Created bucket: {}", bucket);
            } catch (S3Exception ce) {
                // 409 или BucketAlreadyOwnedByYou — параллельный create, это ок
                String code = ce.awsErrorDetails() != null
                        ? ce.awsErrorDetails().errorCode() : null;
                if (ce.statusCode() != 409 && !"BucketAlreadyOwnedByYou".equals(code)) {
                    throw ce;
                }
            }
        }
        knownBuckets.add(bucket);
    }

     public URL presignPut(String bucket, String key, Duration ttl) {
        ensureBucketExists(bucket);
        var req = PutObjectPresignRequest.builder()
                .signatureDuration(ttl)
                .putObjectRequest(b -> b.bucket(bucket).key(key))
                .build();
        return s3Presigner.presignPutObject(req).url();
    }

     public URL presignGet(String bucket, String key, Duration ttl) {
        var req = GetObjectPresignRequest.builder()
                .signatureDuration(ttl)
                .getObjectRequest(b -> b.bucket(bucket).key(key))
                .build();
        return s3Presigner.presignGetObject(req).url();
    }

    public void delete(String bucket, String key) {
        s3Client.deleteObject(b -> b.bucket(bucket).key(key));
    }

    public void evictFromCache(String bucket) {
        knownBuckets.remove(bucket);
    }
}