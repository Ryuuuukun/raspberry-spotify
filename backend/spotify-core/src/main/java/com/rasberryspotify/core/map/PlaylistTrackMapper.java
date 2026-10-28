package com.rasberryspotify.core.map;

import com.rasberryspotify.core.dto.playlist.PlaylistTrackResponse;
import com.rasberryspotify.core.entities.PlaylistTrack;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlaylistTrackMapper {

    @Mapping(source = "track.id", target = "trackId")
    @Mapping(source = "track.title", target = "title")
    @Mapping(source = "track.coverArt", target = "coverArt")
    @Mapping(source = "track.durationMs", target = "durationMs")
    PlaylistTrackResponse toResponse(PlaylistTrack playlistTrack);

}
