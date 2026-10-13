package com.rasberryspotify.core.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class TrackArtistId implements Serializable {
    @Column(name = "track_id")
    private UUID trackId;

    @Column(name = "artist_id")
    private UUID artistId;
}
