package com.rasberryspotify.core.dto.playlist;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PlaylistResponse {
    private UUID id;
    private String title;
    private String description;
    private String coverArt;
    private String visibility;

    private UUID ownerId;
    private String ownerUsername;

    private List<PlaylistTrackResponse> tracks;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
