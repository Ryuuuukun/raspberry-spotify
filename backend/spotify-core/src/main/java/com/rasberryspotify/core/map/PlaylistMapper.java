package com.rasberryspotify.core.map;

import com.rasberryspotify.core.dto.playlist.PlaylistResponse;
import com.rasberryspotify.core.dto.playlist.PlaylistUpdateRequest;
import com.rasberryspotify.core.entities.Playlist;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = { UserMapper.class })
public interface PlaylistMapper {
    PlaylistResponse toResponse(Playlist playlist);
    List<PlaylistResponse> toResponses(List<Playlist> playlists);

    void updatePlaylistFromRequest(PlaylistUpdateRequest request, @MappingTarget Playlist playlist);
}
