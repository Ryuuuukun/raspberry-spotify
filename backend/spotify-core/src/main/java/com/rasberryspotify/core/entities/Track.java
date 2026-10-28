package com.rasberryspotify.core.entities;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "tracks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Track {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(name = "cover_art", length = 512)
    private String coverArt;

    @Column(name = "audio_url", length = 512, nullable = false)
    private String audioUrl;

    @Column(columnDefinition = "TEXT")
    private String lyrics;

    @Column(name = "duration_ms", nullable = false)
    private Integer durationMs;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Builder.Default
    @Column(name = "is_public")
    private Boolean isPublic = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private OffsetDateTime updatedAt;

}