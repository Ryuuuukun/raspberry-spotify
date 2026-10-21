package com.rasberryspotify.core.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "playlist_tracks", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"playlist_id", "track_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PlaylistTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;

    @Column(nullable = false)
    private Integer position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by")
    private User addedBy;

    @Column(name = "added_at", insertable = false, updatable = false)
    private OffsetDateTime addedAt;
}
