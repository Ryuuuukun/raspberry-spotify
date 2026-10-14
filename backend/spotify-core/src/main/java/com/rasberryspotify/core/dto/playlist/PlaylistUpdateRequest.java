package com.rasberryspotify.core.dto.playlist;
import jakarta.validation.constraints.Pattern;

import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PlaylistUpdateRequest {
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    @Size(max = 5000, message = "Description must be at most 5000 characters")
    private String description;

    @Size(max = 512, message = "Cover URL must be at most 512 characters")
    private String coverArt;

    @Pattern(
            regexp = "public|private|unlisted",
            message = "Visibility must be one of: public, private, unlisted"
    )
    private String visibility;
}
