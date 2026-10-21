package com.rasberryspotify.core.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "playlists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "cover_art", length = 512)
    private String coverArt;

    @Builder.Default
    @Column(length = 50)
    private String visibility = "public";

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PlaylistTrack> tracks;
}