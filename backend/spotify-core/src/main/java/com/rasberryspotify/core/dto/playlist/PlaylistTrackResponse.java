package com.rasberryspotify.core.dto.playlist;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistTrackResponse {
    private UUID trackId;
    private String title;
    private String coverArt;
    private Integer durationMs;
    private Integer position;
}
