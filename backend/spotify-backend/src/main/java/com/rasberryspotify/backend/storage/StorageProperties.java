package com.rasberryspotify.backend.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.cloud.aws.s3")
public class StorageProperties {
    private String bucket;
    private String publicEndpoint;
    private boolean pathStyleAccessEnabled = true;

    public String getBucket() { return bucket; }
    public void setBucket(String bucket) { this.bucket = bucket; }

    public String getPublicEndpoint() { return publicEndpoint; }
    public void setPublicEndpoint(String v) { this.publicEndpoint = v; }

    public boolean isPathStyleAccessEnabled() { return pathStyleAccessEnabled; }
    public void setPathStyleAccessEnabled(boolean v) { this.pathStyleAccessEnabled = v; }
}