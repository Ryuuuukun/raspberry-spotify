package com.rasberryspotify.core.dto.track;

import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackResponse {
    private UUID id;
    private String title;
    private String audioUrl;
    private String coverArt;
    private Integer durationMs;
    private LocalDate releaseDate;
    private Boolean isPublic;
    private String lyrics;

    private UUID uploadedById;
    private String uploadedByUsername;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
