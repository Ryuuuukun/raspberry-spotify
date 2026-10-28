package com.rasberryspotify.backend.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.cloud.aws.s3")
public class StorageProperties {
    private String bucket;
    public String getBucket(){
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
