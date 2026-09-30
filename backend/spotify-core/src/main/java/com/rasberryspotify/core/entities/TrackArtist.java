package com.rasberryspotify.core.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "track_artists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TrackArtist {
    @EmbeddedId
    @EqualsAndHashCode.Include
    private TrackArtistId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("trackId")
    @JoinColumn(name = "track_id")
    private Track track;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    @MapsId("artistId")
    private Artist artist;

    @Builder.Default
    @Column(length = 100)
    private String role = "main";

    @Builder.Default
    private Integer position = 0;
}
