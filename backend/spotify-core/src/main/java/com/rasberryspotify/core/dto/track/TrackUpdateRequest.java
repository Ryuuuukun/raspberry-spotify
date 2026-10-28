package com.rasberryspotify.core.dto.track;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackUpdateRequest {
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    @Size(max = 512, message = "Audio URL must be at most 512 characters")
    private String audioUrl;

    @Size(max = 512, message = "Cover URL must be at most 512 characters")
    private String coverArt;

    @Positive(message = "Duration must be positive")
    private Integer durationMs;

    private LocalDate releaseDate;

    private Boolean isPublic;

    @Size(max = 50000, message = "Lyrics must be at most 50000 characters")
    private String lyrics;
}
